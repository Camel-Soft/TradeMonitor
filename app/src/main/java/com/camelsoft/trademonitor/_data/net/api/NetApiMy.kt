package com.camelsoft.trademonitor._data.net.api

import com.camelsoft.trademonitor._presentation.models.user.MUserSign
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetApiMy {
    @POST("/v1/signup")
    suspend fun signUp(@Body body: MUserSign): Response<String>

    @POST("/v1/signin")
    suspend fun signIn(@Body body: MUserSign): Response<String>

    @GET("/v1/hello")
    suspend fun hello(): Response<String>

    @GET("/v1/helloauthboth")
    suspend fun helloAuthBoth(): Response<String>
}