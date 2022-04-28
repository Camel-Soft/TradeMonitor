package com.camelsoft.trademonitor._presentation.utils.scan

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.Settings

fun barcodeAutoCorrection(barcode: String): String {
    try {
        if (barcode.isEmpty()) return barcode
        if (barcode.length <= 5 || barcode.length >=14) return barcode

        val prefix = Settings().getPrefix()
        val s1 = barcode.substring(0,1);
        val s2 = barcode.substring(1,2);

        when (barcode.length) {
            6 -> {
                // UPC E - без 0 и без контрольки - 162356
                val digit = EanUpcCheckDigit("0$barcode")
                return "0$barcode${digit.checkDigit}"
            }
            7 -> {
                // UPC E - без контрольки - 0598745
                // UPC E - без 0 - 2654589 (начало с 1 или 2)
                // EAN 8 - без контрольки - 4623567
                if (barcode.substring(0,2) == prefix) return barcode
                if (s1 == "1" || s1 == "2") {
                    val digit = EanUpcCheckDigit("0${barcode.substring(0,6)}")
                    return "0${barcode.substring(0,6)}${digit.checkDigit}"
                }
                val digit = EanUpcCheckDigit(barcode)
                return "$barcode${digit.checkDigit}"
            }
            8 -> {
                // UPC E - с двумя 00 и без контрольки - 00598745
                // UPC E - 02654589
                // EAN 8 - 46235678
                if (barcode.substring(0,2) == prefix) return barcode.substring(0,7)
                if (s1 == "0" && s2 == "0") {
                    val digit = EanUpcCheckDigit(barcode.substring(1))
                    return "${barcode.substring(1)}${digit.checkDigit}"
                }
                val digit = EanUpcCheckDigit(barcode.substring(0,7))
                return "${barcode.substring(0,7)}${digit.checkDigit}"
            }
            9 -> {
                // UPC E - с двумя 00 - 006598745
                if (barcode.substring(0,2) == prefix) return barcode.substring(0,7)
                if (s1 == "0" && s2 == "0") {
                    val digit = EanUpcCheckDigit(barcode.substring(1,8))
                    return barcode.substring(1,8)+digit.checkDigit
                }
                return barcode
            }
            10 -> {
                // UPC A - без 0 и без контрольки - 1254659875
                if (barcode.substring(0,2) == prefix) return barcode.substring(0,7)
                if (s1 != "0") {
                    val digit = EanUpcCheckDigit("0$barcode")
                    return "${"0$barcode"}${digit.checkDigit}"
                }
                return barcode
            }
            11 -> {
                // UPC A - без контрольки - 01254659875
                // UPC A - без 0 - 12546598756
                if (barcode.substring(0,2) == prefix) return barcode.substring(0,7)
                return if (s1 == "0") {
                    val digit = EanUpcCheckDigit(barcode)
                    "${barcode}${digit.checkDigit}"
                } else {
                    val digit = EanUpcCheckDigit("0${barcode.substring(0,10)}")
                    return "0${barcode.substring(0,10)}${digit.checkDigit}"
                }
            }
            12 -> {
                // UPC A - с двумя 00 и без контрольки - 001254659875
                // UPC A - норм - 012546598756
                // EAN 13 без контрольки - 462356985478
                if (s1 == "0" && s2 == "0") {
                    val digit = EanUpcCheckDigit(barcode.substring(1))
                    return "${barcode.substring(1)}${digit.checkDigit}"
                }
                if (s1 == "0") {
                    val digit = EanUpcCheckDigit(barcode.substring(0,11))
                    return "${barcode.substring(0,11)}${digit.checkDigit}"
                }
                val digit = EanUpcCheckDigit(barcode)
                return barcode+digit.checkDigit
            }
            13 -> {
                // UPC A с лишним лидирующим 0 - 0012546598756
                // EAN 13 - 4623569854782
                if (s1 == "0" && s2 == "0") {
                    val digit = EanUpcCheckDigit(barcode.substring(1,12))
                    return "${barcode.substring(1,12)}${digit.checkDigit}"
                }
                val digit = EanUpcCheckDigit(barcode.substring(0,12))
                return "${barcode.substring(0,12)}${digit.checkDigit}"
            }
            else -> return barcode
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        throw Exception(getAppContext().resources.getString(R.string.error_in)+" barcodeAutoCorrection: "+e.message)
    }
}