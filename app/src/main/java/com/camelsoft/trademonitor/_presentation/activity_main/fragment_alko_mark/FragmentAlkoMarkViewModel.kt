package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor._presentation.models.*
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.utils.scan.getScanFromDataMatrix
import com.camelsoft.trademonitor._presentation.utils.trim001d
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.api.repo.IGoods
import com.camelsoft.trademonitor._presentation.utils.mixPrcString
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.settings.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FragmentAlkoMarkViewModel @Inject constructor(
    private val useCaseStorageAlkoMarkDelete: UseCaseStorageAlkoMarkDelete,
    private val useCaseStorageAlkoMarkGetAll: UseCaseStorageAlkoMarkGetAll,
    private val useCaseStorageAlkoMarkInsertOrUpdate: UseCaseStorageAlkoMarkInsertOrUpdate,
    private val useCaseStorageAlkoMarkUpdate: UseCaseStorageAlkoMarkUpdate,
    private val useCaseStorageAlkoCollUpdate: UseCaseStorageAlkoCollUpdate,
    private val settings: Settings,
    private val iGoods: IGoods
): ViewModel() {
    private val _eventUiAlkoMark =  Channel<EventUiAlkoMark>()
    val eventUiAlkoMark = _eventUiAlkoMark.receiveAsFlow()

    private var lastPrice: String? = null

    init {
        lastPrice = settings.getPrice()
    }

    override fun onCleared() {
        super.onCleared()
        settings.putPrice(lastPrice)
    }

    // Список товаров
    private val _listAlkoMark = MutableLiveData<List<MAlkoMark>>()
    val listAlkoMark: LiveData<List<MAlkoMark>> = _listAlkoMark

    private var countAlkoMarks = 0

    fun onEventAlkoMark(eventVmAlkoMark: EventVmAlkoMark) {
        try {
            when(eventVmAlkoMark) {
                is EventVmAlkoMark.OnInsertOrUpdateAlkoMark -> {
                    viewModelScope.launch {
                        val returnAlkoMark = useCaseStorageAlkoMarkInsertOrUpdate.execute(newAlkoMark = createNewAlkoMark(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll, scan = eventVmAlkoMark.scan))
                        // Запрос к репозиторию
                        launch {
                            getFromRepo(returnAlkoMark = returnAlkoMark, id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                        }
                        _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                        _listAlkoMark.value?.let {
                            val scrollPos = it.indexOf(returnAlkoMark)
                            if (it.isNotEmpty()) {
                                if (returnAlkoMark.id != 0L && scrollPos >= 0) sendEventUiAlkoMark(EventUiAlkoMark.ScrollToPos(scrollPos))
                                else sendEventUiAlkoMark(EventUiAlkoMark.ScrollToPos(it.size-1))
                            }
                            countAlkoMarks = it.count()
                            useCaseStorageAlkoCollUpdate.execute(alkoColl = MAlkoColl(
                                id_coll = eventVmAlkoMark.parentAlkoColl.id_coll,
                                created = eventVmAlkoMark.parentAlkoColl.created,
                                changed = System.currentTimeMillis(),
                                total = countAlkoMarks,
                                note = eventVmAlkoMark.parentAlkoColl.note
                            )
                            )
                        }
                    }
                }
                is EventVmAlkoMark.OnInsertOrUpdateAlkoMarks -> {
                    viewModelScope.launch {
                        var returnAlkoMark: MAlkoMark? = null
                        eventVmAlkoMark.scanList.forEach { mScan ->
                            returnAlkoMark = useCaseStorageAlkoMarkInsertOrUpdate.execute(newAlkoMark = createNewAlkoMark(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll, scan = mScan))
                            // Запрос к репозиторию
                            launch {
                                returnAlkoMark?.let { mAlkoMark ->
                                    getFromRepo(returnAlkoMark = mAlkoMark, id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                                }
                            }
                        }
                        _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                        _listAlkoMark.value?.let {
                            val scrollPos = it.indexOf(returnAlkoMark)
                            if (it.isNotEmpty()) {
                                if (returnAlkoMark?.id != 0L && scrollPos >= 0) sendEventUiAlkoMark(EventUiAlkoMark.ScrollToPos(scrollPos))
                                else sendEventUiAlkoMark(EventUiAlkoMark.ScrollToPos(it.size-1))
                            }
                            countAlkoMarks = it.count()
                            useCaseStorageAlkoCollUpdate.execute(alkoColl = MAlkoColl(
                                id_coll = eventVmAlkoMark.parentAlkoColl.id_coll,
                                created = eventVmAlkoMark.parentAlkoColl.created,
                                changed = System.currentTimeMillis(),
                                total = countAlkoMarks,
                                note = eventVmAlkoMark.parentAlkoColl.note
                            )
                            )
                        }
                    }
                }
                is EventVmAlkoMark.OnUpdateAlkoMark -> {
                    viewModelScope.launch {
                        _listAlkoMark.value?.let {
                            if (it.isNotEmpty()) useCaseStorageAlkoMarkUpdate.execute(alkoMark = eventVmAlkoMark.alkoMark)
                            _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                            _listAlkoMark.value?.let {
                                countAlkoMarks = it.count()
                                useCaseStorageAlkoCollUpdate.execute(alkoColl = MAlkoColl(
                                    id_coll = eventVmAlkoMark.parentAlkoColl.id_coll,
                                    created = eventVmAlkoMark.parentAlkoColl.created,
                                    changed = System.currentTimeMillis(),
                                    total = countAlkoMarks,
                                    note = eventVmAlkoMark.parentAlkoColl.note
                                )
                                )
                            }
                        }
                    }
                }
                is EventVmAlkoMark.OnDeleteAlkoMark -> {
                    viewModelScope.launch {
                        _listAlkoMark.value?.let {
                            if (it.isNotEmpty()) useCaseStorageAlkoMarkDelete.execute(alkoMark = it[eventVmAlkoMark.pos])
                            _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                            _listAlkoMark.value?.let {
                                countAlkoMarks = it.count()
                                useCaseStorageAlkoCollUpdate.execute(alkoColl = MAlkoColl(
                                    id_coll = eventVmAlkoMark.parentAlkoColl.id_coll,
                                    created = eventVmAlkoMark.parentAlkoColl.created,
                                    changed = System.currentTimeMillis(),
                                    total = countAlkoMarks,
                                    note = eventVmAlkoMark.parentAlkoColl.note
                                )
                                )
                            }
                        }
                    }
                }
                is EventVmAlkoMark.OnGetAlkoMarks -> {
                    viewModelScope.launch {
                        _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                    }
                }
                is EventVmAlkoMark.OnPublishPrice -> {
                    sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.gray_200, null) ))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sendEventUiAlkoMark(EventUiAlkoMark.ShowErrorUi(App.getAppContext().resources.getString(R.string.error_in)+
                    " FragmentAlkoMarkViewModel.onEventAlkoMark: "+e.message))
        }
    }

    private fun createNewAlkoMark(id_coll: Long, scan: MScan): MAlkoMark {
        var ss = Pair("", "")
        if (scan.format == "DATA_MATRIX") ss = getScanFromDataMatrix(scan.scancode.trim001d())
        return MAlkoMark(
            id = Date().time,
            id_coll = id_coll,
            marka = scan.scancode.trim001d(),
            marka_type = scan.format,
            scancode = ss.first,
            scancode_type = ss.second,
            cena = 0F,
            note = "",
            name = "",
            quantity = 1F,
            type = "",
            status_code = 0,
            holder_color = "white"
        )
    }

    private suspend fun getFromRepo(returnAlkoMark: MAlkoMark, id_coll: Long) {
        try {
            if ((returnAlkoMark.name.isEmpty() || returnAlkoMark.cena == 0F) && returnAlkoMark.scancode.isNotEmpty()) {
                when (val result = iGoods.getGoodsBig(MGoodsBig(scancod = returnAlkoMark.scancode))) {
                    is EventsGoods.Success -> {
                        val mGoodsBig = result.data
                        lastPrice = mixPrcString(nomer = mGoodsBig.prc_number, dateTurn = mGoodsBig.prc_date, time = mGoodsBig.prc_time)
                        lastPrice?.let {
                            sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = it, color = getAppContext().resources.getColor(R.color.green_300, null) ))
                        }?: sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.red_300, null) ))

                        useCaseStorageAlkoMarkUpdate.execute(alkoMark = mapAlkoMark(mAlkoMark = returnAlkoMark, mGoodsBig = mGoodsBig))
                        _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = id_coll)
                    }
                    is EventsGoods.UnSuccess -> {
                        sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.red_300, null) ))
                    }
                    is EventsGoods.Update -> {
                        sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.red_300, null) ))
                    }
                    is EventsGoods.Info -> {
                        sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.red_300, null) ))
                    }
                    is EventsGoods.Error -> {
                        sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.red_300, null) ))
                    }
                }
            }
            else
                sendEventUiAlkoMark(EventUiAlkoMark.PublishPrice(price = lastPrice, color = getAppContext().resources.getColor(R.color.gray_200, null) ))
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[FragmentAlkoMarkViewModel.getFromRepo] ${e.localizedMessage}")
        }
    }

    private fun sendEventUiAlkoMark(eventUiAlkoMark: EventUiAlkoMark) {
        viewModelScope.launch {
            _eventUiAlkoMark.send(eventUiAlkoMark)
        }
    }
}