package com.camelsoft.trademonitor._presentation.activity_main

sealed class EventsVmMain {
    data class PutToken(val token: String?): EventsVmMain()
}