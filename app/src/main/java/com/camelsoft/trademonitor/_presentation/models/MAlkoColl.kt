package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.String

@Parcelize
data class MAlkoColl (
    val id_coll: Long,
    val created: Long,
    val changed: Long,
    val total: Int,
    val note: String
): Parcelable