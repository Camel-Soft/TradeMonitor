package com.camelsoft.trademonitor._presentation.activity_main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._domain.api.ITokenVerifier
import com.camelsoft.trademonitor.common.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(
    private val settings: Settings,
    private val tokenVerifier: ITokenVerifier
    ): ViewModel() {

    private val _onEventsUi = Channel<EventsUiMain>()
    val onEventsUi = _onEventsUi.receiveAsFlow()


    private val observerToToken = Observer<String?> {

    }

    init {
        //token.observeForever(observerToToken)
    }

    override fun onCleared() {
        //token.removeObserver(observerToToken)
        super.onCleared()
    }

    fun onEventsVm(event: EventsVmMain) {
        try {
            when (event) {
                //is EventsVmMain.PutToken -> { _token.value = event.token }
                else -> {}
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            sendEventUiMain(EventsUiMain.ShowError("[ActivityMainViewModel.onEventVm] ${e.localizedMessage}"))
        }
    }

    private fun sendEventUiMain(event: EventsUiMain) {
        viewModelScope.launch {
            _onEventsUi.send(event)
        }
    }
}
