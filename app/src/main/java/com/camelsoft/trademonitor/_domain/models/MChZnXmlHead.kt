package com.camelsoft.trademonitor._domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MChZnXmlHead(
    var innMy: String = "",
    var dateDoc: Long = 0L,
    var withdrawalType: String = "PACKING"
): Parcelable
