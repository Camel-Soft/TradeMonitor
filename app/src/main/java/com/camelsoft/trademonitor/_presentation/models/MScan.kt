package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.String

@Parcelize
data class MScan(
    var scancode: String = "",
    var format: String = ""
): Parcelable