package com.camelsoft.trademonitor._presentation.activity_main.fragment_main

sealed class EventsVmMain {
    object VerifyTaskPrice : EventsVmMain()
    object VerifyTaskAlko : EventsVmMain()
    object VerifyTaskChecker : EventsVmMain()
}
