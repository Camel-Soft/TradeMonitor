package com.camelsoft.trademonitor._data.net.api.retro

import com.camelsoft.trademonitor._presentation.models.MMessage
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface NetApiInSouthUpload {
    @Multipart
    @POST("/v1/upload/{south}")
    suspend fun inSouthUpload(
        @Part file: MultipartBody.Part,
        @Path("south") south: String
    ): Response<MMessage>
}
