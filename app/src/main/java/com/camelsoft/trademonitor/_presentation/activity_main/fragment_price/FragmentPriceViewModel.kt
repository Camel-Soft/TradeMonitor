package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.IPrice
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPriceViewModel @Inject constructor(
    private val iPrice: IPrice
) : ViewModel() {

    private val _eventUiPrice =  Channel<EventUiPrice>()
    val eventUiPrice = _eventUiPrice.receiveAsFlow()

    // Список сборок
    private val _listPriceColl = MutableLiveData<List<MPriceColl>>()
    val listPriceColl: LiveData<List<MPriceColl>> = _listPriceColl

    init {
        viewModelScope.launch {
            _listPriceColl.value = iPrice.getPriceCollAll()
        }
    }

    fun onEventPrice(eventVmPrice: EventVmPrice) {
        try {
            when(eventVmPrice) {
                is EventVmPrice.OnAddCollClick -> {
                    viewModelScope.launch {
                        iPrice.insertPriceColl(priceColl = createNewColl())
                        _listPriceColl.value = iPrice.getPriceCollAll()
                        _listPriceColl.value?.let {
                            if (it.isNotEmpty()) sendEventUiPrice(EventUiPrice.ScrollToPos(it.size-1))
                        }
                    }
                }
                is EventVmPrice.OnUpdateCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            iPrice.updatePriceColl(priceColl = updateColl(it[eventVmPrice.pos], eventVmPrice.newNote))
                            _listPriceColl.value = iPrice.getPriceCollAll()
                            if (it.isNotEmpty()) sendEventUiPrice(EventUiPrice.ScrollToPos(eventVmPrice.pos))
                        }
                    }
                }
                is EventVmPrice.OnDeleteCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            iPrice.deletePriceColl(priceColl = it[eventVmPrice.pos])
                            _listPriceColl.value = iPrice.getPriceCollAll()
                            if (it.isNotEmpty()) sendEventUiPrice(EventUiPrice.ScrollToPos(eventVmPrice.pos-1))
                        }
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUiPrice(EventUiPrice.ShowError(getAppContext().resources.getString(R.string.error_in)+
                    " FragmentPriceViewModel.onEventPrice: "+e.message))
        }
    }

    private fun updateColl(priceColl: MPriceColl, newNote: String) = MPriceColl(
        id_coll = priceColl.id_coll,
        created = priceColl.created,
        changed = priceColl.changed,
        total = priceColl.total,
        note = newNote
        )

    private fun createNewColl(): MPriceColl {
        val timeStamp = System.currentTimeMillis()
        return MPriceColl(
            id_coll = 0L,
            created = timeStamp,
            changed = timeStamp,
            total = 0,
            note = getAppContext().resources.getString(R.string.collection)+"_$timeStamp"
        )
    }

    private fun sendEventUiPrice(eventUiPrice: EventUiPrice) {
        viewModelScope.launch {
            _eventUiPrice.send(eventUiPrice)
        }
    }
}