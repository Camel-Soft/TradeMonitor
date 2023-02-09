package com.camelsoft.trademonitor._presentation.models

import com.google.gson.annotations.SerializedName

data class MInn(
    @SerializedName("inn") val inn: String,
    @SerializedName("name") val name: String
)
