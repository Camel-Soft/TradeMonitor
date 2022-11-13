package com.camelsoft.trademonitor._presentation.activity_main.fragment_sign

sealed class EventsVmSign {
    data class SignUp(val EmlPassInf: Triple<String, String, Boolean>): EventsVmSign()
    data class SignIn(val EmlPass: Pair<String, String>): EventsVmSign()
}
