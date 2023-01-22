package com.camelsoft.trademonitor._domain.api.offl_dbf

import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig

interface ISearchOnArtkl {
    suspend fun openArtkl(artklBaseName: String, artklIndexName: String?): Boolean
    suspend fun searchArtkl(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
    suspend fun closeArtkl(artklBaseName: String)
}
