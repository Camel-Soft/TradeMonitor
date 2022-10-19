package com.camelsoft.trademonitor._presentation.models

import com.google.gson.annotations.SerializedName

data class MId(
    @SerializedName("sdk")
    var sdk: Int,
    @SerializedName("id")
    var id: String,
)
