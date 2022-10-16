package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor.common.events.EventsGoods

interface IGoods {
    suspend fun getGoodsBig(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
}
