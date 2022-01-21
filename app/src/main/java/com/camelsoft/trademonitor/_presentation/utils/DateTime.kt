package com.camelsoft.trademonitor._presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeToString(timeStamp: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy  HH:mm:ss", Locale.getDefault())
    return dateFormat.format(timeStamp)
}