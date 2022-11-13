package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor._presentation.api.IGoods
import com.camelsoft.trademonitor._presentation.models.*
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._presentation.models.price.MPriceGoods
import com.camelsoft.trademonitor._presentation.utils.genColorIdFromList
import com.camelsoft.trademonitor._presentation.utils.scan.checkBarcode
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.Settings
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FragmentPriceGoodsViewModel @Inject constructor(
    private val useCaseStorageGoodsDelete: UseCaseStorageGoodsDelete,
    private val useCaseStorageGoodsInsertOrUpdate: UseCaseStorageGoodsInsertOrUpdate,
    private val useCaseStorageGoodsUpdate: UseCaseStorageGoodsUpdate,
    private val useCaseStorageGoodsGetAll: UseCaseStorageGoodsGetAll,
    private val useCaseStorageCollUpdate: UseCaseStorageCollUpdate,
    private val settings: Settings,
    private val iGoods: IGoods
): ViewModel() {

    private val _eventUiGoods =  Channel<EventUiGoods>()
    val eventUiGoods = _eventUiGoods.receiveAsFlow()

    // Список товаров
    private val _listPriceGoods = MutableLiveData<List<MPriceGoods>>()
    val listPriceGoods: LiveData<List<MPriceGoods>> = _listPriceGoods

    private var countGoodes = 0

    fun onEventGoods(eventVmGoods: EventVmGoods) {
        try {
            when(eventVmGoods) {
                is EventVmGoods.OnInsertOrUpdateGoods -> {
                    viewModelScope.launch {
                        val returnPriceGoods = useCaseStorageGoodsInsertOrUpdate.execute(newPriceGoods = createNewGoods(id_coll = eventVmGoods.parentColl.id_coll, scan = eventVmGoods.scan))
                        // Запрос к репозиторию
                        launch {
                            getFromRepo(returnPriceGoods = returnPriceGoods, id_coll = eventVmGoods.parentColl.id_coll)
                        }
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.parentColl.id_coll)
                        _listPriceGoods.value?.let {
                            val scrollPos = it.indexOf(returnPriceGoods)
                            if (it.isNotEmpty()) {
                                if (returnPriceGoods.id != 0L && scrollPos >= 0) sendEventUiGoods(EventUiGoods.ScrollToPos(scrollPos))
                                else sendEventUiGoods(EventUiGoods.ScrollToPos(it.size-1))
                            }
                            countGoodes = it.count()
                            useCaseStorageCollUpdate.execute(priceColl = MPriceColl(
                                id_coll = eventVmGoods.parentColl.id_coll,
                                created = eventVmGoods.parentColl.created,
                                changed = System.currentTimeMillis(),
                                total = countGoodes,
                                note = eventVmGoods.parentColl.note
                            )
                            )
                        }
                    }
                }
                is EventVmGoods.OnInsertOrUpdateGoodes -> {
                    viewModelScope.launch {
                        var returnPriceGoods: MPriceGoods? = null
                        eventVmGoods.scanList.forEach { mScan ->
                            returnPriceGoods = useCaseStorageGoodsInsertOrUpdate.execute(newPriceGoods = createNewGoods(id_coll = eventVmGoods.parentColl.id_coll, scan = mScan))
                            // Запрос к репозиторию
                            launch {
                                returnPriceGoods?.let { mPriceGoods ->
                                    getFromRepo(returnPriceGoods = mPriceGoods, id_coll = eventVmGoods.parentColl.id_coll)
                                }
                            }
                        }
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.parentColl.id_coll)
                        _listPriceGoods.value?.let {
                            val scrollPos = it.indexOf(returnPriceGoods)
                            if (it.isNotEmpty()) {
                                if (returnPriceGoods?.id != 0L && scrollPos >= 0) sendEventUiGoods(EventUiGoods.ScrollToPos(scrollPos))
                                else sendEventUiGoods(EventUiGoods.ScrollToPos(it.size-1))
                            }
                            countGoodes = it.count()
                            useCaseStorageCollUpdate.execute(priceColl = MPriceColl(
                                id_coll = eventVmGoods.parentColl.id_coll,
                                created = eventVmGoods.parentColl.created,
                                changed = System.currentTimeMillis(),
                                total = countGoodes,
                                note = eventVmGoods.parentColl.note
                            )
                            )
                        }
                    }
                }
                is EventVmGoods.OnInsertOrUpdateGoodsHandmade -> {
                    viewModelScope.launch {
                        val returnPriceGoods = useCaseStorageGoodsInsertOrUpdate.execute(newPriceGoods = eventVmGoods.priceGoods)
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.parentColl.id_coll)
                        _listPriceGoods.value?.let {
                            val scrollPos = it.indexOf(returnPriceGoods)
                            if (it.isNotEmpty()) {
                                if (returnPriceGoods.id != 0L && scrollPos >= 0) sendEventUiGoods(EventUiGoods.ScrollToPos(scrollPos))
                                else sendEventUiGoods(EventUiGoods.ScrollToPos(it.size-1))
                            }
                            countGoodes = it.count()
                            useCaseStorageCollUpdate.execute(priceColl = MPriceColl(
                                id_coll = eventVmGoods.parentColl.id_coll,
                                created = eventVmGoods.parentColl.created,
                                changed = System.currentTimeMillis(),
                                total = countGoodes,
                                note = eventVmGoods.parentColl.note
                            )
                            )
                        }
                    }
                }
                is EventVmGoods.OnUpdateGoods -> {
                    viewModelScope.launch {
                        _listPriceGoods.value?.let {
                            if (it.isNotEmpty()) useCaseStorageGoodsUpdate.execute(priceGoods = eventVmGoods.priceGoods)
                            _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.parentColl.id_coll)
                            _listPriceGoods.value?.let {
                                countGoodes = it.count()
                                useCaseStorageCollUpdate.execute(priceColl = MPriceColl(
                                    id_coll = eventVmGoods.parentColl.id_coll,
                                    created = eventVmGoods.parentColl.created,
                                    changed = System.currentTimeMillis(),
                                    total = countGoodes,
                                    note = eventVmGoods.parentColl.note
                                )
                                )
                            }
                        }
                    }
                }
                is EventVmGoods.OnDeleteGoods -> {
                    viewModelScope.launch {
                        _listPriceGoods.value?.let {
                            if (it.isNotEmpty()) useCaseStorageGoodsDelete.execute(priceGoods = it[eventVmGoods.pos])
                            _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.parentColl.id_coll)
                            _listPriceGoods.value?.let {
                                countGoodes = it.count()
                                useCaseStorageCollUpdate.execute(priceColl = MPriceColl(
                                    id_coll = eventVmGoods.parentColl.id_coll,
                                    created = eventVmGoods.parentColl.created,
                                    changed = System.currentTimeMillis(),
                                    total = countGoodes,
                                    note = eventVmGoods.parentColl.note
                                )
                                )
                            }
                        }
                    }
                }
                is EventVmGoods.OnGetGoodes -> {
                    viewModelScope.launch {
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = eventVmGoods.parentColl.id_coll)
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUiGoods(
                EventUiGoods.ShowErrorUi(
                    App.getAppContext().resources.getString(R.string.error_in)+
                    " FragmentPriceGoodsViewModel.onEventGoods: "+e.message))
        }
    }

    private fun createNewGoods(id_coll: Long, scan: MScan): MPriceGoods {
        var colorIdFromList = "0"
        _listPriceGoods.value?.let {
            colorIdFromList = genColorIdFromList(it)
        }
        return MPriceGoods(
            id = Date().time,
            id_coll = id_coll,
            scancode = scan.scancode,
            scancode_type = scan.format,
            cena = 0F,
            note = "",
            name = "",
            quantity = 1F,
            ed_izm = "",
            status_code = if (checkBarcode(prefix = settings.getPrefix(), barcode = scan.scancode)) 0 else 1,
            holder_color = colorIdFromList
        )
    }

    private suspend fun getFromRepo(returnPriceGoods: MPriceGoods, id_coll: Long) {
        try {
            if ((returnPriceGoods.name.isEmpty() || returnPriceGoods.cena == 0F) && returnPriceGoods.scancode.isNotEmpty()) {
                when (val result = iGoods.getGoodsBig(MGoodsBig(scancod = returnPriceGoods.scancode))) {
                    is EventsGoods.Success -> {
                        useCaseStorageGoodsUpdate.execute(priceGoods = mapPriceGoods(mPriceGoods = returnPriceGoods, mGoodsBig = result.data))
                        _listPriceGoods.value = useCaseStorageGoodsGetAll.execute(id_coll = id_coll)
                    }
                    is EventsGoods.UnSuccess -> {}
                    is EventsGoods.Update -> {}
                    is EventsGoods.Info -> {}
                    is EventsGoods.Error -> {}
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[FragmentPriceGoodsViewModel.getFromRepo] ${e.localizedMessage}")
        }
    }

    private fun sendEventUiGoods(eventUiGoods: EventUiGoods) {
        viewModelScope.launch {
            _eventUiGoods.send(eventUiGoods)
        }
    }
}
