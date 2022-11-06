package com.camelsoft.trademonitor._presentation.models.user

import com.google.gson.annotations.SerializedName

data class MUser(
    @SerializedName("issuer") val issuer: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("isCorp") val isCorp: Boolean,
    @SerializedName("notes") val notes: String,
    @SerializedName("isActiveDev") val isActiveDev: Boolean,
    @SerializedName("isActiveSrv") val isActiveSrv: Boolean,
    @SerializedName("licLevelDev") val licLevelDev: Int,            // Если тестовый период, то -1
    @SerializedName("licLevelSrv") val licLevelSrv: Int,            // Если тестовый период, то -1
    @SerializedName("expTime") val expTime: Long,                   // Фиксированный срок, когда закончится
    @SerializedName("devs") var devs: List<MDev>,
    @SerializedName("srvs") var srvs: List<MSrv>
)
