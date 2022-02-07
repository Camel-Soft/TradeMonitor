package com.camelsoft.trademonitor._domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MPriceColl(
    val id_coll: Long,
    val created: Long,
    val changed: Long,
    val total: Int,
    val note: String
): Parcelable