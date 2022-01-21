package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

sealed class EventUi {
    data class ShowError(val message: String): EventUi()
    data class ScrollToPos(val position: Int): EventUi()
}