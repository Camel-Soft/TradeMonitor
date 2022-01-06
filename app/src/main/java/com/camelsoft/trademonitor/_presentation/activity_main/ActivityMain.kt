package com.camelsoft.trademonitor._presentation.activity_main

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera.ActivityCamera
import com.camelsoft.trademonitor._presentation.utils.reqPermissions
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.utils.dialogs.showPermShouldGive
import com.camelsoft.trademonitor.common.resource.ResSync
import com.camelsoft.trademonitor.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import android.content.Intent

import android.app.Activity

import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.ActivityCameraList


class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Верхняя панель
        binding.activityMainContent.mainToolbar.setNavigationIcon(R.drawable.img_menu_24)
        setSupportActionBar(binding.activityMainContent.mainToolbar)
        supportActionBar?.title = resources.getString(R.string.title_menu)

        // Нажатия Navigation-списка
        binding.mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navMenuDir -> { camStart() }
                R.id.navMenuSettings -> { camListStart() }
                R.id.navMenuExit -> { finish() }
                else -> {}
            }
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        getPermissions()
    }

    // Нажатия кнопок верхнего меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    // Запрос прав
    private fun getPermissions() {
        reqPermissions(this) { result ->
            when (result) {
                is ResSync.Success -> {
                    result.data?.let {
                        if (!result.data)
                            showPermShouldGive(this) { finish() }
                    }
                }
                is ResSync.Error -> {
                    val backupMessage = resources.getString(R.string.error_in)+
                            " onResume.ReqPermissions: "+
                            resources.getString(R.string.error_text_unknown)
                    showError(this, result.message?:backupMessage) {
                        finish()
                    }
                }
            }
        }
    }

    // Выход
    private var back_pressed: Long = 0
    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed()
        else
            Toast.makeText(baseContext, resources.getString(R.string.back_pressed), Toast.LENGTH_SHORT).show()
        back_pressed = System.currentTimeMillis()
    }



    // Фотосканер
    private fun camStart() {
        try {
            if (!applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                showInfo(this, resources.getString(R.string.attention_cameras)) {}
                return
            }

            val scanOptions = ScanOptions()
            scanOptions.captureActivity = ActivityCamera::class.java
            scanOptions.setDesiredBarcodeFormats(
                ScanOptions.EAN_13,
                ScanOptions.EAN_8,
                ScanOptions.UPC_A,
                ScanOptions.UPC_E)
            scanOptions.setCameraId(0)
            scanOptions.setBeepEnabled(true)
            scanOptions.setPrompt("")
            camLauncher.launch(scanOptions)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+" _________.camStart: "+e.message) {}
        }
    }

    // Фотосканер результат
    private val camLauncher = registerForActivityResult( ScanContract() ) {
        if (it.contents != null)
            Toast.makeText(this, "Scanned: "+it.contents+" Format: ${it.formatName}", Toast.LENGTH_LONG).show()
    }


    // Фотосканер Список
    private fun camListStart() {
        val intent = Intent(this, ActivityCameraList::class.java)
        startActivity(intent)

    }


    // Фотосканер Список результат




}