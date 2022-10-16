package com.camelsoft.trademonitor._data.net.api

import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetApiScan {
    @POST("/v1/scan")
    suspend fun responseGoodsBig(@Body body: MGoodsBig): Response<MGoodsBig>
}
