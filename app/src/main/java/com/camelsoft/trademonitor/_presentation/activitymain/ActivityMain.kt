package com.camelsoft.trademonitor._presentation.activitymain

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.barcode_scanners.ScanCamera
import com.camelsoft.trademonitor._presentation.utils.reqPermissions
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showPermShouldGive
import com.camelsoft.trademonitor.common.resource.ResSync
import com.camelsoft.trademonitor.databinding.ActivityMainBinding

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
                R.id.navMenuDir -> { ScanCamera { Toast.makeText(this, it, Toast.LENGTH_LONG).show() } }
                R.id.navMenuSettings -> {  }
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


//    private val barcodeLauncher = registerForActivityResult( ScanContract() ) {
//        if (it.contents == null) {
//            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
//            } else {
//
//            Toast.makeText(
//                this,
//                "Scanned: " + it.contents+" Format: ${it.formatName}",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//
//
//
//    private fun scanStart() {
//
//        val options = ScanOptions()
//        options.captureActivity = ActivityCamera::class.java
//        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
//        options.setCameraId(0)
//        options.setBeepEnabled(true)
//        options.setPrompt("")
//        barcodeLauncher.launch(options)
//
//
//    }






}