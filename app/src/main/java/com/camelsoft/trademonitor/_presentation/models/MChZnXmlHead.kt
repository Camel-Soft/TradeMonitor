package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.String

@Parcelize
data class MChZnXmlHead(
    var innMy: String = "",
    var dateDoc: Long = 0L,
    var withdrawalType: String = "PACKING"
): Parcelable
