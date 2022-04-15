package com.camelsoft.trademonitor._presentation.utils.scan

fun checkBarcode(prefix: String, barcode: String): Boolean {
    if (prefix == barcode.substring(0,2)) {
        // Весовой
        return when (barcode.count()) {
            7 -> true
            13 -> barcode.substring(12) == EanUpcCheckDigit(barcode.substring(0,12)).checkDigit
            else -> false
        }
    }
    else {
        // Не весовой
        return when (barcode.count()) {
            8 -> if (barcode.substring(0,1) == "1" || barcode.substring(0,1) == "2") false
            else barcode.substring(7) == EanUpcCheckDigit(barcode.substring(0,7)).checkDigit

            12 -> if (barcode.substring(0,1) != "0") false
            else barcode.substring(11) == EanUpcCheckDigit(barcode.substring(0,11)).checkDigit

            13 -> barcode.substring(12) == EanUpcCheckDigit(barcode.substring(0,12)).checkDigit

            else -> false
        }
    }
}