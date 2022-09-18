package com.camelsoft.trademonitor._presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun timeToString(timeStamp: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy  HH:mm:ss", Locale.getDefault())
    return dateFormat.format(timeStamp)
}

fun timeToStringShort(timeStamp: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormat.format(timeStamp)
}

fun timeToChZn(timeStamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(timeStamp)
}