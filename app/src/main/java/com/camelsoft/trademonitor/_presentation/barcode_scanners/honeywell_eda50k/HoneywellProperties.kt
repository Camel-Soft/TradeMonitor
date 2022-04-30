package com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k

import com.honeywell.aidc.BarcodeReader

fun honeyScanProp1D(): Map<String, Any> {
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

fun honeyScanProp2D(): Map<String, Any> {
    val scanProperties = HashMap<String, Any>()
    scanProperties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = false
    scanProperties[BarcodeReader.PROPERTY_EAN_8_ENABLED] = false
    scanProperties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = false
    scanProperties[BarcodeReader.PROPERTY_UPC_E_ENABLED] = false
    scanProperties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
    scanProperties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = true
    return scanProperties
}

fun honeyScanProp1D2D(): Map<String, Any> {
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