package com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k

import com.honeywell.aidc.BarcodeReader

fun honeyScanPropShort(): Map<String, Any> {
    val scanProperties = HashMap<String, Any>()
    scanProperties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_EAN_8_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
    scanProperties[BarcodeReader.PROPERTY_UPC_E_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = false
    scanProperties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = false
    scanProperties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = false
    return scanProperties
}

fun honeyScanPropLong(): Map<String, Any> {
    val scanProperties = HashMap<String, Any>()
    scanProperties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_EAN_8_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
    scanProperties[BarcodeReader.PROPERTY_UPC_E_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = true
    return scanProperties
}