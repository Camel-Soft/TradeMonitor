package com.camelsoft.trademonitor._data.net.api

import retrofit2.Response
import retrofit2.http.GET

interface NetApiHello {
    @GET("/v1/hello")
    suspend fun hello(): Response<String>

    @GET("/v1/helloauthboth")
    suspend fun helloAuthBoth(): Response<String>
}
