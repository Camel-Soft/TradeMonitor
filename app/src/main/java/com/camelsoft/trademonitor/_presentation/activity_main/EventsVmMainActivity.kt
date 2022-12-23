package com.camelsoft.trademonitor._presentation.activity_main

sealed class EventsVmMainActivity {
    object Logout : EventsVmMainActivity()
    object VerifyTaskPrice : EventsVmMainActivity()
    object VerifyTaskAlko : EventsVmMainActivity()
    object VerifyTaskChecker : EventsVmMainActivity()
    object VerifyTaskOffline : EventsVmMainActivity()
}
