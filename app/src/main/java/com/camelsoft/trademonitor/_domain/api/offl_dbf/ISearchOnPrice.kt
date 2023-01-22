package com.camelsoft.trademonitor._domain.api.offl_dbf

import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig

interface ISearchOnPrice {
    suspend fun openPrice(priceBaseName: String, priceIndexName: String?): Boolean
    suspend fun searchPrice(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
    suspend fun closePrice(priceBaseName: String)
}
