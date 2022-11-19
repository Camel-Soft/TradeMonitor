package com.camelsoft.trademonitor._data.net.api.retro

import com.camelsoft.trademonitor._presentation.models.MMessage
import retrofit2.Response
import retrofit2.http.GET

interface NetApiHello {
    @GET("/v1/hello")
    suspend fun hello(): Response<MMessage>

    @GET("/v1/helloauthboth")
    suspend fun helloAuthBoth(): Response<MMessage>
}
