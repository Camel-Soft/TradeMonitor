package com.camelsoft.trademonitor._presentation.activity_main

import com.camelsoft.trademonitor._presentation.models.user.MUser

sealed class EventsUiMain {
    data class ShowError(val message: String): EventsUiMain()
    data class ShowInfo(val message: String): EventsUiMain()
    data class ShowToast(val message: String): EventsUiMain()
    data class LogIn(val mUser: MUser): EventsUiMain()
    object LogOut: EventsUiMain()
}
