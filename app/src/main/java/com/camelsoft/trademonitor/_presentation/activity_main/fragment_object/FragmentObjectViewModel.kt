package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.EventsNet
import com.camelsoft.trademonitor._presentation.api.repo.IObject
import com.camelsoft.trademonitor._presentation.models.MAddress
import com.camelsoft.trademonitor.common.settings.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentObjectViewModel @Inject constructor(
    private val settings: Settings,
    private val iObject: IObject
): ViewModel() {

    private val _eventUiObject =  Channel<EventsUiObject>()
    val eventUiObject = _eventUiObject.receiveAsFlow()

    private val _listMAddress = MutableLiveData<List<MAddress>>()
    val listMAddress: LiveData<List<MAddress>> = _listMAddress

    init {
        getList()
        restoreFromSettings()
    }

    fun eventsVm(event: EventsVmObject) {
        when (event) {
            is EventsVmObject.RefreshAddressList -> getList()
            is EventsVmObject.ApplyAddress -> {
                settings.putConnSrvLoc(address = event.mAddress.address)
                settings.putConnSrvLocName(name = event.mAddress.name)
            }
        }
    }

    private fun restoreFromSettings() {
        val address = settings.getConnSrvLoc()
        val name = settings.getConnSrvLocName()
        if (address.isNotBlank() && name.isBlank())
            sendEventUiObject(EventsUiObject.PublicCurrent(MAddress(address = "", name = address)))
        else
            sendEventUiObject(EventsUiObject.PublicCurrent(MAddress(address = address, name = name)))
    }

    private fun searchBest(): Boolean {
        val address = settings.getConnSrvLoc().trim().lowercase()
        listMAddress.value?.forEach { mAddress ->
            if (mAddress.address.trim().lowercase() == address) {
                settings.putConnSrvLocName(mAddress.name)
                return true
            }
        }
        return false
    }

    private fun getList() {
        viewModelScope.launch {
            sendEventUiObject(EventsUiObject.Progress(true))
            when (val answer = iObject.getObjects()) {
                is EventsNet.Success -> {
                    sendEventUiObject(EventsUiObject.Progress(false))
                    val list = answer.data
                    _listMAddress.value = list
                    if (searchBest()) restoreFromSettings()
                }
                is EventsNet.Info -> {
                    sendEventUiObject(EventsUiObject.Progress(false))
                    sendEventUiObject(EventsUiObject.ShowInfo(answer.message))
                }
                is EventsNet.Error -> {
                    sendEventUiObject(EventsUiObject.Progress(false))
                    sendEventUiObject(EventsUiObject.ShowError(answer.message))
                }
            }
        }
    }

    private fun sendEventUiObject(eventUiObject: EventsUiObject) {
        viewModelScope.launch {
            _eventUiObject.send(eventUiObject)
        }
    }
}
