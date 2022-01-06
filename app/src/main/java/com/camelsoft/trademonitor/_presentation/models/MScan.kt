package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MScan(
    var scancode: String = "",
    var format: String = ""
): Parcelable