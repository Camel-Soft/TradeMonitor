package com.camelsoft.trademonitor._presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun toMoney(numbers: Float): String {
    val symbols = DecimalFormatSymbols()
    symbols.decimalSeparator = '.'
    val formatter = DecimalFormat("###.##", symbols)
    return formatter.format(numbers)
}

fun toQuantity(numbers: Float): String {
    val symbols = DecimalFormatSymbols()
    symbols.decimalSeparator = '.'
    val formatter = DecimalFormat("###.###", symbols)
    return formatter.format(numbers)
}

fun toQuantity(string: String): String {
    val symbols = DecimalFormatSymbols()
    symbols.decimalSeparator = '.'
    val formatter = DecimalFormat("###.###", symbols)
    var numbers = 0F
    try {numbers = string.toFloat()} catch (_: Exception) {}
    return formatter.format(numbers)
}
