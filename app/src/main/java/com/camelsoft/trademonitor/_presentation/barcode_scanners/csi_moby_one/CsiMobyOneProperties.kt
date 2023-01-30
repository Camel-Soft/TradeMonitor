package com.camelsoft.trademonitor._presentation.barcode_scanners.csi_moby_one

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

// id = 1
fun csiScanProp1D(): Map<String, Any> {
    val scanProperties = HashMap<String, Any>()
    scanProperties[getAppContext().resources.getString(R.string.ean_13)] = true
    scanProperties[getAppContext().resources.getString(R.string.ean_8)] = true
    scanProperties[getAppContext().resources.getString(R.string.upc_a)] = true
    scanProperties[getAppContext().resources.getString(R.string.upc_e)] = true
    return scanProperties
}

// id = 2
fun csiScanProp2D(): Map<String, Any> {
    val scanProperties = HashMap<String, Any>()
    scanProperties[getAppContext().resources.getString(R.string.data_matrix)] = true
    scanProperties[getAppContext().resources.getString(R.string.qr_code)] = true
    scanProperties[getAppContext().resources.getString(R.string.pdf_417)] = true
    return scanProperties
}

// id = 3
fun csiScanPropChecker(): Map<String, Any> {
    val scanProperties = HashMap<String, Any>()
    scanProperties[getAppContext().resources.getString(R.string.ean_13)] = true
    scanProperties[getAppContext().resources.getString(R.string.ean_8)] = true
    scanProperties[getAppContext().resources.getString(R.string.upc_a)] = true
    scanProperties[getAppContext().resources.getString(R.string.upc_e)] = true
    scanProperties[getAppContext().resources.getString(R.string.data_matrix)] = true
    return scanProperties
}
