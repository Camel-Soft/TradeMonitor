package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

sealed class EventUiGoods {
    data class ShowErrorUi(val message: String): EventUiGoods()
    data class ScrollToPos(val position: Int): EventUiGoods()
    data class PublishPrice(val price: String?, val color: Int): EventUiGoods()
}
