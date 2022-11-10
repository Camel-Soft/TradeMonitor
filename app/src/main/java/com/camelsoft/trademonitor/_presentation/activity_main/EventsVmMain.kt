package com.camelsoft.trademonitor._presentation.activity_main

sealed class EventsVmMain {
    data class SignUp(val EmlPassInf: Triple<String, String, Boolean>): EventsVmMain()
    data class SignIn(val EmlPass: Pair<String, String>): EventsVmMain()
}
