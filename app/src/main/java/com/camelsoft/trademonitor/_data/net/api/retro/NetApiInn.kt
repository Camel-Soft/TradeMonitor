package com.camelsoft.trademonitor._data.net.api.retro

import com.camelsoft.trademonitor._presentation.models.MMessage
import retrofit2.Response
import retrofit2.http.GET

interface NetApiInn {
    @GET("/v1/getconfinn")
    suspend fun getConfInn(): Response<MMessage>
}
