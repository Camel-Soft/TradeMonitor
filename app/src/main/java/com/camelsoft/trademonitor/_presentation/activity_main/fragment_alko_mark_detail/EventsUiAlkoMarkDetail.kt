package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark_detail

import com.camelsoft.trademonitor._presentation.models.MGoodsBig

sealed class EventsUiAlkoMarkDetail {
    data class Success(val mGoodsBig: MGoodsBig): EventsUiAlkoMarkDetail()
    data class UnSuccess(val message: String): EventsUiAlkoMarkDetail()
    data class Update(val message: String): EventsUiAlkoMarkDetail()
    data class Progress(val show: Boolean): EventsUiAlkoMarkDetail()
    data class ShowInfo(val message: String): EventsUiAlkoMarkDetail()
    data class ShowError(val message: String): EventsUiAlkoMarkDetail()
}
