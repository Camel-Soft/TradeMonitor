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

fun toMoneyKop(numbers: Float): String {
    val v = toMoney(numbers).toFloat()*100
    val formatter = DecimalFormat("###")
    return formatter.format(v)
}