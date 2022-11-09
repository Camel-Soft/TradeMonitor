package com.camelsoft.trademonitor._presentation.activity_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._presentation.api.ITokenUser
import com.camelsoft.trademonitor._presentation.models.user.MUser
import com.camelsoft.trademonitor.common.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(
    private val settings: Settings,
    private val tokenUser: ITokenUser
    ): ViewModel() {

    private val _onEventsUi = Channel<EventsUiMain>()
    val onEventsUi = _onEventsUi.receiveAsFlow()

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    private val _mUser = MutableLiveData<MUser?>()
    val mUser: LiveData<MUser?> = _mUser

    private val observerToToken = Observer<String?> { token->
        if (token == null) {
            _mUser.value = null
            sendEventUiMain(EventsUiMain.LogOut)
        }
        else {
            viewModelScope.launch {
                if (!tokenUser.authUserDev(token)) {
                    _mUser.value = null
                    _token.value = null
                    sendEventUiMain(EventsUiMain.LogOut)
                }
                else {
                    _mUser.value = tokenUser.tokenToMUser(token)
                    _mUser.value?.let {
                        sendEventUiMain(EventsUiMain.LogIn(it))
                    }
                }
            }
        }
    }

    init {
        _token.value = settings.getToken()
        token.observeForever(observerToToken)
    }

    override fun onCleared() {
        settings.putToken(_token.value)
        token.removeObserver(observerToToken)
        super.onCleared()
    }

    fun onEventsVm(event: EventsVmMain) {
        try {
            when (event) {
                is EventsVmMain.PutToken -> { _token.value = event.token }
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
