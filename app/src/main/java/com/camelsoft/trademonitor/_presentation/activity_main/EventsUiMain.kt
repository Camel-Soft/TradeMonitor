package com.camelsoft.trademonitor._presentation.activity_main

sealed class EventsUiMain {
    data class ShowError(val message: String): EventsUiMain()
    data class ShowInfo(val message: String): EventsUiMain()
}
