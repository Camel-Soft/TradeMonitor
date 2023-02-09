package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportExcelSheet
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportJsonGoodes
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportSouthRevision
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollDelete
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollGetAll
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollInsert
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollUpdate
import com.camelsoft.trademonitor._presentation.api.repo.IInSouthUpload
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsOkInEr
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPriceViewModel @Inject constructor(
    private val useCaseStorageCollDelete: UseCaseStorageCollDelete,
    private val useCaseStorageCollInsert: UseCaseStorageCollInsert,
    private val useCaseStorageCollUpdate: UseCaseStorageCollUpdate,
    private val useCaseStorageCollGetAll: UseCaseStorageCollGetAll,
    private val settings: Settings,
    private val useCaseExportExcelSheet: UseCaseExportExcelSheet,
    private val useCaseExportSouthRevision: UseCaseExportSouthRevision,
    private val useCaseExportJsonGoodes: UseCaseExportJsonGoodes,
    private val iInSouthUpload: IInSouthUpload
) : ViewModel() {

    private val _eventUiPrice =  Channel<EventUiPrice>()
    val eventUiPrice = _eventUiPrice.receiveAsFlow()

    // Список сборок
    private val _listPriceColl = MutableLiveData<List<MPriceColl>>()
    val listPriceColl: LiveData<List<MPriceColl>> = _listPriceColl

    fun onEventPrice(eventVmPrice: EventVmPrice) {
        try {
            when(eventVmPrice) {
                is EventVmPrice.OnAddCollClick -> {
                    viewModelScope.launch {
                        useCaseStorageCollInsert.execute(priceColl = createNewColl())
                        _listPriceColl.value = useCaseStorageCollGetAll.execute()
                        _listPriceColl.value?.let {
                            if (it.isNotEmpty()) sendEventUiPrice(EventUiPrice.ScrollToPos(it.size-1))
                        }
                    }
                }
                is EventVmPrice.OnUpdateCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            if (it.isNotEmpty()) useCaseStorageCollUpdate.execute(priceColl = updateColl(it[eventVmPrice.pos], eventVmPrice.newNote))
                            _listPriceColl.value = useCaseStorageCollGetAll.execute()
                        }
                    }
                }
                is EventVmPrice.OnDeleteCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            if (it.isNotEmpty()) useCaseStorageCollDelete.execute(priceColl = it[eventVmPrice.pos])
                            _listPriceColl.value = useCaseStorageCollGetAll.execute()
                        }
                    }
                }
                is EventVmPrice.OnShareCollClick -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            when (settings.getExportFileFormat()) {
                                "excel" -> {
                                    when (val answerExcel = useCaseExportExcelSheet.execute(priceColl = it[eventVmPrice.pos])) {
                                        is EventsSync.Success -> sendEventUiPrice(EventUiPrice.ShareFile(file = answerExcel.data, sign = it[eventVmPrice.pos].note))
                                        is EventsSync.Error -> sendEventUiPrice(EventUiPrice.ShowErrorUi(answerExcel.message))
                                    }
                                }
                                "south_rev" -> {
                                    when (val answerSouRev = useCaseExportSouthRevision.execute(priceColl = it[eventVmPrice.pos])) {
                                        is EventsSync.Success -> sendEventUiPrice(EventUiPrice.ShareFile(file = answerSouRev.data, sign = it[eventVmPrice.pos].note))
                                        is EventsSync.Error -> sendEventUiPrice(EventUiPrice.ShowErrorUi(answerSouRev.message))
                                    }
                                }
                                "south_rev_one" -> sendEventUiPrice(EventUiPrice.ConfirmSouthUpload(south = "south1", pos = eventVmPrice.pos))
                                "south_rev_two" -> sendEventUiPrice(EventUiPrice.ConfirmSouthUpload(south = "south2", pos = eventVmPrice.pos))
                                "json" -> {
                                    when (val answerJsonGoodes = useCaseExportJsonGoodes.execute(priceColl = it[eventVmPrice.pos])) {
                                        is EventsSync.Success -> sendEventUiPrice(EventUiPrice.ShareFile(file = answerJsonGoodes.data, sign = it[eventVmPrice.pos].note))
                                        is EventsSync.Error -> sendEventUiPrice(EventUiPrice.ShowErrorUi(answerJsonGoodes.message))
                                    }
                                }
                                "ch_zn" -> {
                                    sendEventUiPrice(EventUiPrice.ShowInfoUi(getAppContext().resources.getString(R.string.info_empty_export_format)))
                                }
                                else -> {
                                    sendEventUiPrice(EventUiPrice.ShowErrorUi(getAppContext().resources.getString(R.string.error_in)+
                                            " FragmentPriceViewModel.onEventPrice.EventVmPrice.OnShareCollClick: "+
                                            getAppContext().resources.getString(R.string.error_export_file_format)))
                                }
                            }
                        }
                    }
                }
                is EventVmPrice.OnSouthUpload -> {
                    viewModelScope.launch {
                        _listPriceColl.value?.let {
                            sendEventUiPrice(EventUiPrice.ShowProgress(
                                show = true,
                                textTop = getAppContext().resources.getString(R.string.stage_copy),
                                textBottom = getAppContext().resources.getString(R.string.wait)
                            ))
                            when (val answerSouRev = useCaseExportSouthRevision.execute(priceColl = it[eventVmPrice.pos])) {
                                is EventsSync.Success -> {
                                    when ( val answerUpload = iInSouthUpload.inSouthUpload(file = answerSouRev.data, south = eventVmPrice.south)) {
                                        is EventsOkInEr.Success -> {
                                            sendEventUiPrice(EventUiPrice.ShowProgress(show = false, textTop = "", textBottom = "" ))
                                            sendEventUiPrice(EventUiPrice.ShowInfoUi(answerUpload.data))
                                        }
                                        is EventsOkInEr.Info -> {
                                            sendEventUiPrice(EventUiPrice.ShowProgress(show = false, textTop = "", textBottom = "" ))
                                            sendEventUiPrice(EventUiPrice.ShowInfoUi(answerUpload.message))
                                        }
                                        is EventsOkInEr.Error -> {
                                            sendEventUiPrice(EventUiPrice.ShowProgress(show = false, textTop = "", textBottom = "" ))
                                            sendEventUiPrice(EventUiPrice.ShowErrorUi(answerUpload.message))
                                        }
                                    }
                                }
                                is EventsSync.Error -> {
                                    sendEventUiPrice(EventUiPrice.ShowProgress(show = false, textTop = "", textBottom = "" ))
                                    sendEventUiPrice(EventUiPrice.ShowErrorUi(answerSouRev.message))
                                }
                            }
                        }
                    }
                }
                is EventVmPrice.OnGetColl -> {
                    viewModelScope.launch {
                        _listPriceColl.value = useCaseStorageCollGetAll.execute()
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            sendEventUiPrice(EventUiPrice.ShowProgress(show = false, textTop = "", textBottom = "" ))
            sendEventUiPrice(EventUiPrice.ShowErrorUi("[FragmentPriceViewModel.onEventPrice] ${e.localizedMessage}"))
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
