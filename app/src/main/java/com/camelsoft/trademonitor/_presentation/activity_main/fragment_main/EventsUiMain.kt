package com.camelsoft.trademonitor._presentation.activity_main.fragment_main

sealed class EventsUiMain {
    data class HandleTaskPrice(val run: Boolean): EventsUiMain()
    data class HandleTaskAlko(val run: Boolean): EventsUiMain()
}
