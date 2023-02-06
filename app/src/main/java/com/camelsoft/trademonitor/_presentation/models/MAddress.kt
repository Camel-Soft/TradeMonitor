package com.camelsoft.trademonitor._presentation.models

import com.google.gson.annotations.SerializedName

data class MAddress(
    @SerializedName("address") val address: String,
    @SerializedName("name") val name: String
)
