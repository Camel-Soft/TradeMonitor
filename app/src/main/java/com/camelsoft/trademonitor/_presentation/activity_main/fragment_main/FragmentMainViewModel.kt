package com.camelsoft.trademonitor._presentation.activity_main.fragment_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenUserVerifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentMainViewModel @Inject constructor(
    private val tokenUserVerifier: TokenUserVerifier
): ViewModel() {

    private val _eventsUi = Channel<EventsUiMain>()
    val eventsUi = _eventsUi.receiveAsFlow()

    fun eventsVm(event: EventsVmMain) {
        when (event) {
            is EventsVmMain.VerifyTaskPrice -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMain.HandleTaskPrice(run = true))
                else
                    sendEventUi(EventsUiMain.HandleTaskPrice(run = false))
            }
            is EventsVmMain.VerifyTaskAlko -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMain.HandleTaskAlko(run = true))
                else
                    sendEventUi(EventsUiMain.HandleTaskAlko(run = false))
            }
            is EventsVmMain.VerifyTaskChecker -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMain.HandleTaskChecker(run = true))
                else
                    sendEventUi(EventsUiMain.HandleTaskChecker(run = false))
            }
        }
    }

    private fun sendEventUi(event: EventsUiMain) {
        viewModelScope.launch {
            _eventsUi.send(event)
        }
    }
}
