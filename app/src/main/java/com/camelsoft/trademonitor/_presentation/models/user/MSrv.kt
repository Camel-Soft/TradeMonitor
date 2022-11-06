package com.camelsoft.trademonitor._presentation.models.user

import com.google.gson.annotations.SerializedName

data class MSrv(
    @SerializedName("recId") val recId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("srvId1") val srvId1: String,
    @SerializedName("srvId2") val srvId2: String,
    @SerializedName("srvId3") val srvId3: String,
    @SerializedName("srvDateReg") val srvDateReg: Long,
    @SerializedName("srvDateUnreg") val srvDateUnreg: Long,
    @SerializedName("isActiveSrv") val isActiveSrv: Boolean,
    @SerializedName("licLevelSrv") val licLevelSrv: Int,
    @SerializedName("expTimeSrv") val expTimeSrv: Long
)
