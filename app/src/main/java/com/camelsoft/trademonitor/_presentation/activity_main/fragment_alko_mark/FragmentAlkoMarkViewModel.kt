package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MAlkoColl
import com.camelsoft.trademonitor._domain.models.MAlkoMark
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.trim001d
import com.camelsoft.trademonitor.common.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentAlkoMarkViewModel @Inject constructor(
    private val useCaseStorageAlkoMarkDelete: UseCaseStorageAlkoMarkDelete,
    private val useCaseStorageAlkoMarkGetAll: UseCaseStorageAlkoMarkGetAll,
    private val useCaseStorageAlkoMarkInsertOrUpdate: UseCaseStorageAlkoMarkInsertOrUpdate,
    private val useCaseStorageAlkoMarkUpdate: UseCaseStorageAlkoMarkUpdate,
    private val useCaseStorageAlkoCollUpdate: UseCaseStorageAlkoCollUpdate
): ViewModel() {

    private val _eventUiAlkoMark =  Channel<EventUiAlkoMark>()
    val eventUiAlkoMark = _eventUiAlkoMark.receiveAsFlow()

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
                            ))
                        }
                    }
                }
                is EventVmAlkoMark.OnInsertOrUpdateAlkoMarks -> {
                    viewModelScope.launch {
                        var returnAlkoMark: MAlkoMark? = null
                        eventVmAlkoMark.scanList.forEach {
                            returnAlkoMark = useCaseStorageAlkoMarkInsertOrUpdate.execute(newAlkoMark = createNewAlkoMark(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll, scan = it))
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
                            ))
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
                                ))
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
                                ))
                            }
                        }
                    }
                }
                is EventVmAlkoMark.OnGetAlkoMarks -> {
                    viewModelScope.launch {
                        _listAlkoMark.value = useCaseStorageAlkoMarkGetAll.execute(id_coll = eventVmAlkoMark.parentAlkoColl.id_coll)
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUiAlkoMark(EventUiAlkoMark.ShowErrorUi(App.getAppContext().resources.getString(R.string.error_in)+
                    " FragmentAlkoMarkViewModel.onEventAlkoMark: "+e.message))
        }
    }

    private fun createNewAlkoMark(id_coll: Long, scan: MScan): MAlkoMark {
        return MAlkoMark(
            id = 0L,
            id_coll = id_coll,
            marka = scan.scancode.trim001d(),
            marka_type = scan.format,
            scancode = "",
            scancode_type = "",
            cena = 0F,
            note = "",
            name = "",
            quantity = 1F,
            type = "",
            status_code = 0,
            holder_color = "white"
        )
    }

    private fun sendEventUiAlkoMark(eventUiAlkoMark: EventUiAlkoMark) {
        viewModelScope.launch {
            _eventUiAlkoMark.send(eventUiAlkoMark)
        }
    }
}