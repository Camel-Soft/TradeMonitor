package com.camelsoft.trademonitor._presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MOffline(
    var status: Int,
    var info: String,
    var stageCurrent: Int,
    var stageTotal: Int,
    var stageName: String,
    var stagePercent: Int
): Parcelable
