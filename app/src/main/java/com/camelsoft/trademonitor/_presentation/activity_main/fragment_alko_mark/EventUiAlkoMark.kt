package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import com.camelsoft.trademonitor._presentation.models.MGoodsBig

sealed class EventUiAlkoMark {
    data class ShowErrorUi(val message: String): EventUiAlkoMark()
    data class ScrollToPos(val position: Int): EventUiAlkoMark()
    data class PublishGoodsBig(val mGoodsBig: MGoodsBig?): EventUiAlkoMark()
}
