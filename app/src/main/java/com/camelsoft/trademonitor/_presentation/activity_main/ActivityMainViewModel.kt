package com.camelsoft.trademonitor._presentation.activity_main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._presentation.api.ITokenUser
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

    private val observerToToken = Observer<String?> { token->
        if (token == null) Log.d("vm", "token is: null")
        else Log.d("vm", "token is: $token")



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
