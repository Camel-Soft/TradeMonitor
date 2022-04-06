package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MPriceGoods
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageGoodsDelete
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageGoodsGetAll
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageGoodsInsert
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageGoodsUpdate
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor.common.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPriceGoodsViewModel @Inject constructor(
    private val useCaseStorageGoodsDelete: UseCaseStorageGoodsDelete,
    private val useCaseStorageGoodsInsert: UseCaseStorageGoodsInsert,
    private val useCaseStorageGoodsUpdate: UseCaseStorageGoodsUpdate,
    private val useCaseStorageGoodsGetAll: UseCaseStorageGoodsGetAll
): ViewModel() {

    private val _eventUiGoods =  Channel<EventUiGoods>()
    val eventUiGoods = _eventUiGoods.receiveAsFlow()

    // Список товаров
    private val _listPriceGoods = MutableLiveData<List<MPriceGoods>>()
    val listPriceGoods: LiveData<List<MPriceGoods>> = _listPriceGoods

    fun onEventGoods(eventVmGoods: EventVmGoods) {
        try {
            when(eventVmGoods) {
                is EventVmGoods.OnInsertGoods -> {
                    viewModelScope.launch {
                        useCaseStorageGoodsInsert.execute(priceGoods = createNewGoods(eventVmGoods.id_coll, eventVmGoods.scan))
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.id_coll)
                        _listPriceGoods.value?.let {
                            if (it.isNotEmpty()) sendEventUiGoods(EventUiGoods.ScrollToPos(it.size-1))
                        }
                    }
                }
                is EventVmGoods.OnInsertGoodes -> {
                    viewModelScope.launch {
                        eventVmGoods.scanList.forEach {
                            useCaseStorageGoodsInsert.execute(priceGoods = createNewGoods(eventVmGoods.id_coll, it))
                        }
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.id_coll)
                        _listPriceGoods.value?.let {
                            if (it.isNotEmpty()) sendEventUiGoods(EventUiGoods.ScrollToPos(it.size-1))
                        }
                    }
                }
                is EventVmGoods.OnUpdateGoods -> {
                    viewModelScope.launch {
                        _listPriceGoods.value?.let {
                            useCaseStorageGoodsUpdate.execute(priceGoods = eventVmGoods.priceGoods)
                            _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.id_coll)
                            if (it.isNotEmpty()) sendEventUiGoods(EventUiGoods.ScrollToPos(eventVmGoods.pos))
                        }
                    }
                }
                is EventVmGoods.OnDeleteGoods -> {
                    viewModelScope.launch {
                        _listPriceGoods.value?.let {
                            useCaseStorageGoodsDelete.execute(priceGoods = it[eventVmGoods.pos])
                            _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.id_coll)
                            if (it.isNotEmpty()) sendEventUiGoods(EventUiGoods.ScrollToPos(eventVmGoods.pos-1))
                        }
                    }
                }
                is EventVmGoods.OnGetGoodes -> {
                    viewModelScope.launch {
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.id_coll)
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUiGoods(
                EventUiGoods.ShowError(
                    App.getAppContext().resources.getString(R.string.error_in)+
                    " FragmentPriceGoodsViewModel.onEventGoods: "+e.message))
        }
    }

    private fun createNewGoods(id_coll: Long, scan: MScan): MPriceGoods {
        return MPriceGoods(
            id = 0L,
            id_coll = id_coll,
            scancode = scan.scancode,
            scancode_type = scan.format,
            cena = 0F,
            note = "",
            name = ""
        )
    }

    private fun sendEventUiGoods(eventUiGoods: EventUiGoods) {
        viewModelScope.launch {
            _eventUiGoods.send(eventUiGoods)
        }
    }
}