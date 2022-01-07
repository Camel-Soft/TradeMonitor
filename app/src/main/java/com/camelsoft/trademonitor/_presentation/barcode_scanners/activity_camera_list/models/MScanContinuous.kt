package com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models

import android.graphics.Bitmap

data class MScanContinuous(
    var scancode: String,
    var bitmap: Bitmap
)