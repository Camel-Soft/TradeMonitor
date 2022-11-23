package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

sealed class EventUiAlkoMark {
    data class ShowErrorUi(val message: String): EventUiAlkoMark()
    data class ScrollToPos(val position: Int): EventUiAlkoMark()
    data class PublishPrice(val price: String?, val color: Int): EventUiAlkoMark()
}
