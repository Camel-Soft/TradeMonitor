package com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models

import android.os.Parcelable
import com.google.zxing.BarcodeFormat
import kotlinx.parcelize.Parcelize

@Parcelize
data class MBarcodeFormat(
    var barcodeFormat: BarcodeFormat
): Parcelable