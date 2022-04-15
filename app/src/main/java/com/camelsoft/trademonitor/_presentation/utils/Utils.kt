package com.camelsoft.trademonitor._presentation.utils

fun getErrCode(code: Int): Int {
    return code % 100
}

fun getWrkCode(code: Int): Int {
    return code / 100
}

fun <T> genColorIdFromList(list: List<T>): String {
    return if (list.isEmpty()) "0"
    else (list.size % 10).toString()
}