package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Scan(
    var content: String = "",
    var format: String = ""
): Parcelable