package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MStringString(
    var string1: String = "",
    var string2: String = ""
): Parcelable
