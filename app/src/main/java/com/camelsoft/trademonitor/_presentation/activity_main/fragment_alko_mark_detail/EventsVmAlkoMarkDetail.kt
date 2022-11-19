package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark_detail

import com.camelsoft.trademonitor._presentation.models.MGoodsBig

sealed class EventsVmAlkoMarkDetail {
    data class SendRequestGoods(val mGoodsBig: MGoodsBig): EventsVmAlkoMarkDetail()
}
