package com.camelsoft.trademonitor._presentation.utils.scan

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.Settings

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

fun getScanType(sysString: String): String {
    return when(sysString) {
        "EAN_13" -> App.getAppContext().resources.getString(R.string.ean_13)
        "EAN_8" -> App.getAppContext().resources.getString(R.string.ean_8)
        "UPC_A" -> App.getAppContext().resources.getString(R.string.upc_a)
        "UPC_E" -> App.getAppContext().resources.getString(R.string.upc_e)
        "QR_CODE" -> App.getAppContext().resources.getString(R.string.qr_code)
        "PDF_417" -> App.getAppContext().resources.getString(R.string.pdf_417)
        "DATA_MATRIX" -> App.getAppContext().resources.getString(R.string.data_matrix)
        "EAN_13_WEIGHT" -> App.getAppContext().resources.getString(R.string.ean_13_weight)
        "SCANCODE_TYPE_NOT_DEFINED" -> App.getAppContext().resources.getString(R.string.scancode_type_not_defined)
        else -> sysString
    }
}

fun pickBarcodeType(barcode: String): String {
    if (barcode.isEmpty()) return "SCANCODE_TYPE_NOT_DEFINED"
    else {
        val l = barcode.length
        if (l < 7 || l > 13)  return "SCANCODE_TYPE_NOT_DEFINED"
        else {
            if (Settings().getPrefix() == barcode.substring(0,2)) return "EAN_13_WEIGHT"
            else {
                val first = barcode.substring(0,1)
                if (l == 8 && (first == "0" || first == "1" || first == "2")) return "UPC_E"
                else {
                    if (l == 8) return "EAN_8"
                    else {
                        if (l == 12 && (first == "0" || first == "1" || first == "2")) return "UPC_A"
                        else {
                            if (l == 13) return "EAN_13"
                            else return "SCANCODE_TYPE_NOT_DEFINED"
                        }
                    }
                }
            }
        }
    }
}

fun removeHideFromMark(cod: String): String {
    return cod.replace(oldChar = '\u001d', newChar = ' ', ignoreCase = true)

}