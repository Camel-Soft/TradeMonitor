package com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor.databinding.ActivityCameraBinding
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ActivityCamera : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var captureManager: CaptureManager
    private var flagTorch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Верхняя панель
        binding.camToolbar.setNavigationIcon(R.drawable.img_arrow_back_white_24)
        setSupportActionBar(binding.camToolbar)
        supportActionBar?.title = ""
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        captureManager = CaptureManager(this, binding.camDecorBarView)
        captureManager.initializeFromIntent(intent, savedInstanceState)
        captureManager.decode()

        binding.camDecorBarView.setTorchListener(torchListener)
        binding.camBtnTorch.setOnClickListener(btnTorchListener)

        if (!hasFlash()) binding.camBtnTorch.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.camDecorBarView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        captureManager.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private val torchListener = object: DecoratedBarcodeView.TorchListener {
        override fun onTorchOn() {
            binding.camBtnTorch.setImageResource(R.drawable.img_torch_off_white_40)
        }
        override fun onTorchOff() {
            binding.camBtnTorch.setImageResource(R.drawable.img_torch_on_white_40)
        }
    }

    private val btnTorchListener = View.OnClickListener {
        flagTorch = if (!flagTorch) {
            binding.camDecorBarView.setTorchOn()
            true
        } else {
            binding.camDecorBarView.setTorchOff()
            false
        }
    }

    private fun hasFlash(): Boolean {
        return try {
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        } catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCamera.hasFlash: "+e.message) {}
            return false
        }
    }
}

//    // Фотосканер
//    private fun camStart() {
//        try {
//            if (!applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
//                showInfo(this, resources.getString(R.string.attention_cameras)) {}
//                return
//            }
//
//            val scanOptions = ScanOptions()
//            scanOptions.captureActivity = ActivityCamera::class.java
//            scanOptions.setDesiredBarcodeFormats(
//                ScanOptions.EAN_13,
//                ScanOptions.EAN_8,
//                ScanOptions.UPC_A,
//                ScanOptions.UPC_E)
//            scanOptions.setCameraId(0)
//            scanOptions.setBeepEnabled(true)
//            scanOptions.setPrompt("")
//            camLauncher.launch(scanOptions)
//        }catch (e: Exception) {
//            e.printStackTrace()
//            showError(this, resources.getString(R.string.error_in)+" _________.camStart: "+e.message) {}
//        }
//    }
//
//    // Фотосканер результат
//    private val camLauncher = registerForActivityResult( ScanContract() ) {
//        try {
//            if (it.contents != null && it.formatName != null)
//                Toast.makeText(this, "Scancode: ${it.contents}\nFormat: ${it.formatName}", Toast.LENGTH_LONG).show()
//        }catch (e: Exception) {
//            e.printStackTrace()
//            showError(this, resources.getString(R.string.error_in)+" _________.camLauncher: "+e.message) {}
//        }
//    }
