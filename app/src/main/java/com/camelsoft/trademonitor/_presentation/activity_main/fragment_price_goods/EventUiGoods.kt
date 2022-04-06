package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

sealed class EventUiGoods {
    data class ShowError(val message: String): EventUiGoods()
    data class ScrollToPos(val position: Int): EventUiGoods()
}