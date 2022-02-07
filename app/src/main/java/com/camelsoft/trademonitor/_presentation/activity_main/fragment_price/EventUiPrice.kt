package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

sealed class EventUiPrice {
    data class ShowError(val message: String): EventUiPrice()
    data class ScrollToPos(val position: Int): EventUiPrice()
}