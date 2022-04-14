package com.camelsoft.trademonitor._presentation.utils

import java.text.DecimalFormat

fun toMoney(numbers: Float): String {
    val formatter = DecimalFormat("###.##")
    return formatter.format(numbers)
}

fun toQuantity(numbers: Float): String {
    val formatter = DecimalFormat("###.###")
    return formatter.format(numbers)
}