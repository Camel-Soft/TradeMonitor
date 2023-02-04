package com.camelsoft.trademonitor._presentation.activity_main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenUserVerifier
import com.camelsoft.trademonitor._presentation.models.user.MUser
import com.camelsoft.trademonitor._presentation.utils.isAutoLogout
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(
    private val tokenUserVerifier: TokenUserVerifier
    ): ViewModel() {

    private val _eventsUi = Channel<EventsUiMainActivity>()
    val eventsUi = _eventsUi.receiveAsFlow()

    private val observerToMUser = Observer<MUser?> { mUser ->
        try {
            if (mUser == null) sendEventUi(EventsUiMainActivity.LogOut)
            else sendEventUi(EventsUiMainActivity.LogIn(mUser))
        }
        catch (e: Exception) {
            e.printStackTrace()
            sendEventUi(EventsUiMainActivity.ShowError("[ActivityMainViewModel.observerToMUser] ${e.localizedMessage}"))
        }
    }

    init {
        tokenUserVerifier.mUser.observeForever(observerToMUser)
        // Если прошло более 30 дней непрерывного подключения, то делаем АвтоВыход
        if (isAutoLogout()) {
            tokenUserVerifier.setNewToken(null)
            sendEventUi(EventsUiMainActivity.LogOut)
        }
    }

    override fun onCleared() {
        tokenUserVerifier.mUser.removeObserver(observerToMUser)
        super.onCleared()
    }

    fun eventsVm(event: EventsVmMainActivity) {
        when (event) {
            is EventsVmMainActivity.Logout -> {
                tokenUserVerifier.setNewToken(null)
                sendEventUi(EventsUiMainActivity.ShowToast(getAppContext().resources.getString(R.string.logout_go)))
                sendEventUi(EventsUiMainActivity.LogOut)
            }
            is EventsVmMainActivity.VerifyTaskPrice -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMainActivity.HandleTaskPrice(run = true))
                else
                    sendEventUi(EventsUiMainActivity.HandleTaskPrice(run = false))
            }
            is EventsVmMainActivity.VerifyTaskAlko -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMainActivity.HandleTaskAlko(run = true))
                else
                    sendEventUi(EventsUiMainActivity.HandleTaskAlko(run = false))
            }
            is EventsVmMainActivity.VerifyTaskChecker -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMainActivity.HandleTaskChecker(run = true))
                else
                    sendEventUi(EventsUiMainActivity.HandleTaskChecker(run = false))
            }
            is EventsVmMainActivity.VerifyTaskOffline -> {
                if (tokenUserVerifier.verifyExistToken())
                    sendEventUi(EventsUiMainActivity.HandleTaskOffline(run = true))
                else
                    sendEventUi(EventsUiMainActivity.HandleTaskOffline(run = false))
            }
        }
    }

    private fun sendEventUi(event: EventsUiMainActivity) {
        viewModelScope.launch {
            _eventsUi.send(event)
        }
    }
}
