package com.camelsoft.trademonitor._domain.api.offl_dbf

import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig

interface ISearchOnFirm {
    suspend fun openFirm(firmBaseName: String, firmIndexName: String?): Boolean
    suspend fun searchFirm(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig>
    suspend fun closeFirm(firmBaseName: String)
}
