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

    private val _eventUi =  Channel<EventUi>()
    val eventUi = _eventUi.receiveAsFlow()

    // Список сборок
    private val _listPriceColl = MutableLiveData<List<MPriceColl>>()
    val listPriceColl: LiveData<List<MPriceColl>> = _listPriceColl

    init {
        viewModelScope.launch {
            _listPriceColl.value = iPrice.getPriceCollAll()
        }
    }

    fun onEvent(eventVm: EventVm) {
        try {
            when(eventVm) {
                is EventVm.OnAddCollClick -> {
                    viewModelScope.launch {
                        iPrice.insertPriceColl(priceColl = createNewColl())
                        _listPriceColl.value = iPrice.getPriceCollAll()
                        _listPriceColl.value?.let {
                            if (it.isNotEmpty()) sendEventUi(EventUi.ScrollToPos(it.size-1))
                        }
                    }
                }
                is EventVm.OnUpdateCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            iPrice.updatePriceColl(priceColl = updateColl(it[eventVm.pos], eventVm.newNote))
                            _listPriceColl.value = iPrice.getPriceCollAll()
                            if (it.isNotEmpty()) sendEventUi(EventUi.ScrollToPos(eventVm.pos))
                        }
                    }
                }
                is EventVm.OnDeleteCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            iPrice.deletePriceColl(priceColl = it[eventVm.pos])
                            _listPriceColl.value = iPrice.getPriceCollAll()
                            if (it.isNotEmpty()) sendEventUi(EventUi.ScrollToPos(eventVm.pos-1))
                        }
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUi(EventUi.ShowError(getAppContext().resources.getString(R.string.error_in)+
                    " FragmentPriceViewModel.onEvent: "+e.message))
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

    private fun sendEventUi(eventUi: EventUi) {
        viewModelScope.launch {
            _eventUi.send(eventUi)
        }
    }
}