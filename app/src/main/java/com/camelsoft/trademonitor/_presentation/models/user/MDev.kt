package com.camelsoft.trademonitor._presentation.models.user

import com.google.gson.annotations.SerializedName

data class MDev(
    @SerializedName("recId") val recId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("devSdk") val devSdk: Int,
    @SerializedName("devId") val devId: String,
    @SerializedName("devAid") val devAid: String,
    @SerializedName("devDateReg") val devDateReg: Long,
    @SerializedName("devDateUnreg") val devDateUnreg: Long,
    @SerializedName("isActiveDev") val isActiveDev: Boolean,
    @SerializedName("licLevelDev") val licLevelDev: Int,
    @SerializedName("expTimeDev") val expTimeDev: Long
)
