package com.camelsoft.trademonitor._presentation.activity_main

import com.camelsoft.trademonitor._presentation.models.user.MUser

sealed class EventsUiMainActivity {
    data class ShowError(val message: String): EventsUiMainActivity()
    data class ShowInfo(val message: String): EventsUiMainActivity()
    data class ShowToast(val message: String): EventsUiMainActivity()
    data class LogIn(val mUser: MUser): EventsUiMainActivity()
    object LogOut: EventsUiMainActivity()
    data class HandleTaskPrice(val run: Boolean): EventsUiMainActivity()
    data class HandleTaskAlko(val run: Boolean): EventsUiMainActivity()
    data class HandleTaskChecker(val run: Boolean): EventsUiMainActivity()
    data class HandleTaskOffline(val run: Boolean): EventsUiMainActivity()
}
