package com.camelsoft.trademonitor._presentation.activity_main.fragment_sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.api.ITelephony
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.EventsNet
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenUserVerifier
import com.camelsoft.trademonitor._presentation.api.ISign
import com.camelsoft.trademonitor._presentation.models.user.MUserSign
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSignViewModel @Inject constructor(
    private val tokenUserVerifier: TokenUserVerifier,
    private val iSign: ISign,
    private val iTelephony: ITelephony
): ViewModel() {

    private val _eventsUi = Channel<EventsUiSign>()
    val eventsUi = _eventsUi.receiveAsFlow()

    fun eventsVm(event: EventsVmSign) {
        try {
            when (event) {
                // Регистрация
                is EventsVmSign.SignUp -> {
                    viewModelScope.launch {
                        sendEventUi(EventsUiSign.Progress(show = true))
                        val telephony = iTelephony.getTelephonyItems()
                        val mUserSign = MUserSign(
                            email = event.EmlPassInf.first,
                            password = event.EmlPassInf.second,
                            isInforming = event.EmlPassInf.third,
                            devSdk = telephony.sdk,
                            devId = telephony.id,
                            devAid = telephony.aid
                        )
                        when (val result = iSign.signUp(mUserSign = mUserSign)) {
                            is EventsNet.Success -> {
                                sendEventUi(EventsUiSign.Progress(show = false))
                                sendEventUi(EventsUiSign.ShowToast(getAppContext().resources.getString(R.string.signup_go_signin)))
                                signIn(coroutineScope = this, email = event.EmlPassInf.first, password =event.EmlPassInf.second)
                            }
                            is EventsNet.Info -> {
                                sendEventUi(EventsUiSign.Progress(show = false))
                                sendEventUi(EventsUiSign.ShowInfo(result.message))
                            }
                            is EventsNet.Error -> {
                                sendEventUi(EventsUiSign.Progress(show = false))
                                sendEventUi(EventsUiSign.ShowError(result.message))
                            }
                        }
                    }
                }
                // Вход
                is EventsVmSign.SignIn -> {
                    viewModelScope.launch {
                        signIn(coroutineScope = this, email = event.EmlPass.first, password = event.EmlPass.second)
                    }
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            sendEventUi(EventsUiSign.Progress(show = false))
            sendEventUi(EventsUiSign.ShowError("[FragmentSignViewModel.eventsVm] ${e.localizedMessage}"))
        }
    }

    private fun signIn(coroutineScope: CoroutineScope, email: String, password: String) {
        try {
            coroutineScope.launch {
                sendEventUi(EventsUiSign.Progress(show = true))
                val telephony = iTelephony.getTelephonyItems()
                val mUserSign = MUserSign(
                    email = email,
                    password = password,
                    isInforming = false,  // в данной ситуации не учитывается сервером
                    devSdk = telephony.sdk,
                    devId = telephony.id,
                    devAid = telephony.aid
                )
                when (val result = iSign.signIn(mUserSign = mUserSign)) {
                    is EventsNet.Success -> {
                        if (tokenUserVerifier.setNewToken(result.data)) {
                            sendEventUi(EventsUiSign.Progress(show = false))
                            sendEventUi(EventsUiSign.ShowToast(getAppContext().resources.getString(R.string.signin_go)))
                            sendEventUi(EventsUiSign.Close)
                        }
                        else {
                            sendEventUi(EventsUiSign.Progress(show = false))
                            sendEventUi(EventsUiSign.ShowError(getAppContext().resources.getString(R.string.signin_stop)))
                        }
                    }
                    is EventsNet.Info -> {
                        sendEventUi(EventsUiSign.Progress(show = false))
                        sendEventUi(EventsUiSign.ShowInfo(result.message))
                    }
                    is EventsNet.Error -> {
                        sendEventUi(EventsUiSign.Progress(show = false))
                        sendEventUi(EventsUiSign.ShowError(result.message))
                    }
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[FragmentSignViewModel.signIn] ${e.localizedMessage}")
        }
    }

    private fun sendEventUi(event: EventsUiSign) {
        viewModelScope.launch {
            _eventsUi.send(event)
        }
    }
}
