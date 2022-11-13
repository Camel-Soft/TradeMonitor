package com.camelsoft.trademonitor._presentation.models

import com.google.gson.annotations.SerializedName

data class MMessage(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)
