package com.camelsoft.trademonitor._presentation.barcode_scanners

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class ScanCamera(result: (String) -> Unit): AppCompatActivity() {



    init {
        scanStart()
    }

    val barcodeLauncher = registerForActivityResult( ScanContract() ) {
        if (it.contents == null) {
            //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            result("Scanned: " + it.contents+" Format: ${it.formatName}")


//            Toast.makeText(
//                this,
//                "Scanned: " + it.contents+" Format: ${it.formatName}",
//                Toast.LENGTH_LONG
//            ).show()
        }
    }

    fun scanStart() {

        val options = ScanOptions()
        options.captureActivity = ActivityCamera::class.java
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setPrompt("")
        barcodeLauncher.launch(options)


    }






}