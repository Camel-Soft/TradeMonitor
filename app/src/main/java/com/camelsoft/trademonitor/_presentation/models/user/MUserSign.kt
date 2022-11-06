package com.camelsoft.trademonitor._presentation.models.user

import com.google.gson.annotations.SerializedName

data class MUserSign(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("isInforming") val isInforming: Boolean,
    @SerializedName("devSdk") val devSdk: Int,
    @SerializedName("devId") val devId: String,
    @SerializedName("devAid") val devAid: String
)

fun MUserSign.trim(): MUserSign {
    return MUserSign(
        email = this.email.trim(),
        password = this.password.trim(),
        isInforming = this.isInforming,
        devSdk = this.devSdk,
        devId = this.devId.trim(),
        devAid = this.devAid.trim()
    )
}
