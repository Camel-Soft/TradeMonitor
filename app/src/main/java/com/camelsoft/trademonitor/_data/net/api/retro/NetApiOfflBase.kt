package com.camelsoft.trademonitor._data.net.api.retro

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface NetApiOfflBase {
    @GET("/v1/getofflbase")
    suspend fun responseOfflBase(): Response<ResponseBody>
}
