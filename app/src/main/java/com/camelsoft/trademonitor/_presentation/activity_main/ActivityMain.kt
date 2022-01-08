package com.camelsoft.trademonitor._presentation.activity_main

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.reqPermissions
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showPermShouldGive
import com.camelsoft.trademonitor.common.resource.ResSync
import com.camelsoft.trademonitor.databinding.ActivityMainBinding
import android.content.Intent
import android.app.Activity
import com.camelsoft.trademonitor._presentation.activity_settings.ActivitySettings
import com.camelsoft.trademonitor._presentation.api.IResultScan
import com.camelsoft.trademonitor._presentation.barcode_scanners.HoneywellEDA50K
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.ActivityCameraList
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models.MBarcodeFormat
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.dialogs.showInfo
import com.camelsoft.trademonitor.common.Settings
import com.honeywell.aidc.BarcodeReader

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var honeywellEDA50K: HoneywellEDA50K

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        honeywellEDA50K = HoneywellEDA50K(this, honeyListener, getScanProperties())

        // Верхняя панель
        binding.activityMainContent.mainToolbar.setNavigationIcon(R.drawable.img_menu_24)
        setSupportActionBar(binding.activityMainContent.mainToolbar)
        supportActionBar?.title = resources.getString(R.string.title_menu)

        // Нажатия Navigation-списка
        binding.mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navMenuDir -> { camListStart() }
                R.id.navMenuSettings -> {
                    val intent = Intent(this, ActivitySettings::class.java)
                    startActivity(intent)
                }
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






    // HoneywellEDA50K

    override fun onResume() {
        super.onResume()
        honeywellEDA50K.reg()
    }

    override fun onPause() {
        super.onPause()
        honeywellEDA50K.unreg()
    }

    private val honeyListener: IResultScan = object : IResultScan {
        override fun actionScan(scan: ResSync<MScan>) {
            when (scan) {
                is ResSync.Success -> {
                    Toast.makeText(this@ActivityMain, "Scancode: ${scan.data?.scancode}\nFormat: ${scan.data?.format}", Toast.LENGTH_LONG).show()
                }
                is ResSync.Error -> {
                    Toast.makeText(this@ActivityMain, "Error: ${scan.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getScanProperties(): Map<String, Any> {
        val scanProperties = HashMap<String, Any>()
        scanProperties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true)
        scanProperties.put(BarcodeReader.PROPERTY_EAN_8_ENABLED, true)
        scanProperties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true)
        scanProperties.put(BarcodeReader.PROPERTY_UPC_E_ENABLED, true)
        scanProperties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true)
        scanProperties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true)
        scanProperties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, true)
        return scanProperties
    }















    // Фотосканер Список
    private fun camListStart() {
        try {
            val intent = Intent(this, ActivityCameraList::class.java)
            val listFormats = ArrayList<MBarcodeFormat>()
//            listFormats.add(MBarcodeFormat(BarcodeFormat.EAN_13))
//            listFormats.add(MBarcodeFormat(BarcodeFormat.EAN_8))
//            listFormats.add(MBarcodeFormat(BarcodeFormat.UPC_A))
//            listFormats.add(MBarcodeFormat(BarcodeFormat.UPC_E))
            intent.putParcelableArrayListExtra("KEY_LIST_FORMAT", listFormats)
            camListLauncher.launch(intent)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+" _________.camListStart: "+e.message) {}
        }
    }

    // Фотосканер Список результат
    private val camListLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        try {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.getParcelableArrayListExtra<MScan>("KEY_LIST_SCAN")?.let { listScan ->
                    if (listScan.size > 0) {
                        var mes = ""
                        listScan.forEach { scan ->
                            mes += "Scancode: ${scan.scancode} Format: ${scan.format}\n"
                        }
                        Toast.makeText(this, mes, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+" _________.camListLauncher: "+e.message) {}
        }
    }



}