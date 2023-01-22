package com.camelsoft.trademonitor._domain.api.offl_dbf

import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig

/*
    Поиск сначала нужно осуществлять по подгруппе (sgrt), а потом по группе (grt).
    Это связано с замещением значения поля группы (grt) в результирующем MGoods
 */

interface ISearchOnGrt {
    suspend fun openGrt(grtBaseName: String, grtIndexName: String?): Boolean
    suspend fun searchGrt(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
    suspend fun closeGrt(grtBaseName: String)
}
