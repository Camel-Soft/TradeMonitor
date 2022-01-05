package com.camelsoft.trademonitor._presentation.barcode_scanners

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.dialogs.ShowError
import com.camelsoft.trademonitor.databinding.ActivityScannerBinding
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class ActivityScanner : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var captureManager: CaptureManager
    private var flagTorch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Верхняя панель
        binding.scanToolbar.setNavigationIcon(R.drawable.img_arrow_back_white_24)
        setSupportActionBar(binding.scanToolbar)
        supportActionBar?.title = ""
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        captureManager = CaptureManager(this, binding.scanDecorBarView)
        captureManager.initializeFromIntent(intent, savedInstanceState)
        captureManager.decode()

        binding.scanDecorBarView.setTorchListener(object: DecoratedBarcodeView.TorchListener {
            override fun onTorchOn() {
                binding.scanBtnTorch.setImageResource(R.drawable.img_torch_off_white_40)
            }

            override fun onTorchOff() {
                binding.scanBtnTorch.setImageResource(R.drawable.img_torch_on_white_40)
            }
        })

        binding.scanBtnTorch.setOnClickListener {
            flagTorch = if (!flagTorch) {
                binding.scanDecorBarView.setTorchOn()
                true
            } else {
                binding.scanDecorBarView.setTorchOff()
                false
            }
        }

        if (!hasFlash()) binding.scanBtnTorch.visibility = View.INVISIBLE
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
        return binding.scanDecorBarView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        captureManager.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun hasFlash(): Boolean {
        return try {
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        }catch (e: Exception) {
            e.printStackTrace()
            ShowError(this, resources.getString(R.string.error_in)+
                    " ActivityScanner.hasFlash: ${e.message}") {}
            false
        }
    }
}