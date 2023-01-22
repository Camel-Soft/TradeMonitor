package com.camelsoft.trademonitor._domain.api.offl_dbf

import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig

interface ISearchOnScan {
    suspend fun openScan(scanBaseName: String, scanIndexName: String?): Boolean
    suspend fun searchScan(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
    suspend fun closeScan(scanBaseName: String)
}
