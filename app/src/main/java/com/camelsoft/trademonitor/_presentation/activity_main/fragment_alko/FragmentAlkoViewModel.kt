package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExpChZnMilkWithdrawal
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportExcelMarks
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportJsonMarks
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollDelete
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollGetAll
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollInsert
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollUpdate
import com.camelsoft.trademonitor._presentation.api.IChZnParam
import com.camelsoft.trademonitor._presentation.dialogs.specify_ch_zn.makeNoteChZn
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentAlkoViewModel @Inject constructor(
    private val useCaseStorageAlkoCollDelete: UseCaseStorageAlkoCollDelete,
    private val useCaseStorageAlkoCollGetAll: UseCaseStorageAlkoCollGetAll,
    private val useCaseStorageAlkoCollInsert: UseCaseStorageAlkoCollInsert,
    private val useCaseStorageAlkoCollUpdate: UseCaseStorageAlkoCollUpdate,
    private val settings: Settings,
    private val useCaseExportExcelMarks: UseCaseExportExcelMarks,
    private val useCaseExportJsonMarks: UseCaseExportJsonMarks,
    private val useCaseExpChZnMilkWithdrawal: UseCaseExpChZnMilkWithdrawal,
    private val chZnParam: IChZnParam
): ViewModel() {

    private val _eventUiAlkoColl =  Channel<EventUiAlkoColl>()
    val eventUiAlkoColl = _eventUiAlkoColl.receiveAsFlow()

    // Список сборок
    private val _listAlkoColl = MutableLiveData<List<MAlkoColl>>()
    val listAlkoColl: LiveData<List<MAlkoColl>> = _listAlkoColl

    fun onEventAlkoColl(eventVmAlkoColl: EventVmAlkoColl) {
        try {
            when(eventVmAlkoColl) {
                is EventVmAlkoColl.OnAddCollClick -> {
                    viewModelScope.launch {
                        useCaseStorageAlkoCollInsert.execute(alkoColl = createNewColl())
                        _listAlkoColl.value = useCaseStorageAlkoCollGetAll.execute()
                        _listAlkoColl.value?.let {
                            if (it.isNotEmpty()) sendEventUiAlkoColl(EventUiAlkoColl.ScrollToPos(it.size-1))
                        }
                    }
                }
                is EventVmAlkoColl.OnUpdateCollClick -> {
                    viewModelScope.launch {
                        _listAlkoColl.value?.let {
                            if (it.isNotEmpty()) useCaseStorageAlkoCollUpdate.execute(alkoColl = updateColl(it[eventVmAlkoColl.pos], eventVmAlkoColl.newNote))
                            _listAlkoColl.value = useCaseStorageAlkoCollGetAll.execute()
                        }
                    }
                }
                is EventVmAlkoColl.OnDeleteCollClick -> {
                    viewModelScope.launch {
                        _listAlkoColl.value?.let {
                            if (it.isNotEmpty()) useCaseStorageAlkoCollDelete.execute(alkoColl = it[eventVmAlkoColl.pos])
                            _listAlkoColl.value = useCaseStorageAlkoCollGetAll.execute()
                        }
                    }
                }
                is EventVmAlkoColl.OnShareCollClick -> {
                    viewModelScope.launch {
                        _listAlkoColl.value?.let {
                            when (settings.getExportFileFormat()) {
                                "excel" -> {
                                    when (val answerExcel = useCaseExportExcelMarks.execute(alkoColl = it[eventVmAlkoColl.pos])) {
                                        is EventsSync.Success -> sendEventUiAlkoColl(EventUiAlkoColl.ShareFile(file = answerExcel.data, sign = it[eventVmAlkoColl.pos].note))
                                        is EventsSync.Error -> sendEventUiAlkoColl(EventUiAlkoColl.ShowErrorUi(answerExcel.message))
                                    }
                                }
                                "south_rev" -> {
                                    sendEventUiAlkoColl(EventUiAlkoColl.ShowInfoUi(App.getAppContext().resources.getString(R.string.info_empty_export_format)))
                                }
                                "json" -> {
                                    when (val answerJsonMarks = useCaseExportJsonMarks.execute(alkoColl = it[eventVmAlkoColl.pos])) {
                                        is EventsSync.Success -> sendEventUiAlkoColl(EventUiAlkoColl.ShareFile(file = answerJsonMarks.data, sign = it[eventVmAlkoColl.pos].note))
                                        is EventsSync.Error -> sendEventUiAlkoColl(EventUiAlkoColl.ShowErrorUi(answerJsonMarks.message))
                                    }
                                }
                                "ch_zn" -> {
                                    sendEventUiAlkoColl(EventUiAlkoColl.SpecifyChZnUi(position = eventVmAlkoColl.pos, itemsInn = chZnParam.getInnList()))
                                }
                                else -> {
                                    sendEventUiAlkoColl(
                                        EventUiAlkoColl.ShowErrorUi(
                                            App.getAppContext().resources.getString(R.string.error_in)+
                                            " FragmentAlkoViewModel.onEventAlkoColl.EventVmAlkoColl.OnShareCollClick: "+
                                            App.getAppContext().resources.getString(R.string.error_export_file_format)))
                                }
                            }
                        }
                    }
                }
                is EventVmAlkoColl.OnGetColl -> {
                    viewModelScope.launch {
                        _listAlkoColl.value = useCaseStorageAlkoCollGetAll.execute()
                    }
                }
                is EventVmAlkoColl.OnShareChZn -> {
                    viewModelScope.launch {
                        _listAlkoColl.value?.let {
                            when (val answerChZnMilkWithdrawal = useCaseExpChZnMilkWithdrawal.execute(alkoColl = it[eventVmAlkoColl.pos], mChZnXmlHead = eventVmAlkoColl.mChZnXmlHead)) {
                                is EventsSync.Success -> sendEventUiAlkoColl(EventUiAlkoColl.ShareFile(file = answerChZnMilkWithdrawal.data, sign = makeNoteChZn(eventVmAlkoColl.mChZnXmlHead, note = it[eventVmAlkoColl.pos].note)))
                                is EventsSync.Error -> sendEventUiAlkoColl(EventUiAlkoColl.ShowErrorUi(answerChZnMilkWithdrawal.message))
                            }
                        }
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUiAlkoColl(
                EventUiAlkoColl.ShowErrorUi(
                    App.getAppContext().resources.getString(R.string.error_in)+
                    " FragmentAlkoViewModel.onEventAlkoColl: "+e.message))
        }
    }

    private fun updateColl(alkoColl: MAlkoColl, newNote: String) = MAlkoColl(
        id_coll = alkoColl.id_coll,
        created = alkoColl.created,
        changed = alkoColl.changed,
        total = alkoColl.total,
        note = newNote
    )

    private fun createNewColl(): MAlkoColl {
        val timeStamp = System.currentTimeMillis()
        return MAlkoColl(
            id_coll = 0L,
            created = timeStamp,
            changed = timeStamp,
            total = 0,
            note = App.getAppContext().resources.getString(R.string.collection)+"_$timeStamp"
        )
    }

    private fun sendEventUiAlkoColl(eventUiAlkoColl: EventUiAlkoColl) {
        viewModelScope.launch {
            _eventUiAlkoColl.send(eventUiAlkoColl)
        }
    }
}