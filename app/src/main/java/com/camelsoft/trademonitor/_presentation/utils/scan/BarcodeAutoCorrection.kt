package com.camelsoft.trademonitor._presentation.utils.scan

import com.camelsoft.trademonitor.common.Settings

//fun barcodeAutoCorrection(barcode: String): String {
//    if (barcode.isEmpty()) return barcode
//    if (barcode.length <= 5 || barcode.length >=14) return barcode
//
//    val prefix = Settings().getPrefix()
//    val s1 = barcode.substring(0,1);
//    val s2 = barcode.substring(1,2);
//
//    when (barcode.length) {
//        6 -> {
//            val digit = EanUpcCheckDigit("0$barcode")
//            return "0$barcode${digit.checkDigit}"
//        }
//        7 -> {
//            if (barcode.substring(0,2) == prefix) return barcode
//            if (s1 == "1" || s1 == "2") return "0$barcode"
//            val digit = EanUpcCheckDigit(barcode)
//            return "$barcode${digit.checkDigit}"
//        }
//        8 -> {
//
//        }
//        9 -> {
//
//        }
//        10 -> {
//
//        }
//        11 -> {
//
//        }
//        12 -> {
//
//        }
//        13 -> {
//
//        }
//        else -> return barcode
//    }
//}