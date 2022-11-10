package com.camelsoft.trademonitor._presentation.api

import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods

interface IGoods {
    suspend fun getGoodsBig(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
}
