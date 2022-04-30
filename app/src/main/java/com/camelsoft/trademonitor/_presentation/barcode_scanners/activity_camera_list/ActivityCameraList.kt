package com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models.MBarcodeFormat
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models.MScanContinuous
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showInfo
import com.camelsoft.trademonitor.databinding.ActivityCameraListBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class ActivityCameraList : AppCompatActivity() {

    private lateinit var binding: ActivityCameraListBinding
    private lateinit var beepManager: BeepManager
    private var flagTorch = false
    private var lastScancode = ""
    private val listScan = ArrayList<MScan>()
    private val listScanContinuous = ArrayList<MScanContinuous>()
    private lateinit var adapterCameraList: AdapterCameraList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Отключить гашение экрана
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Верхняя панель
        binding.camListToolbar.setNavigationIcon(R.drawable.img_arrow_back_white_24)
        binding.camListToolbar.setCollapseIcon(R.drawable.img_arrow_back_white_24)
        setSupportActionBar(binding.camListToolbar)
        supportActionBar?.title = ""

        beepManager = BeepManager(this)
        binding.camListDecorBarView.setStatusText("")

        // Инициализация адаптера
        adapterCameraList = AdapterCameraList(listScanContinuous, { position ->
            binding.camListTextPrompt.text = listScanContinuous[position].scancode
        }, { position ->
            itemRemove(position)
        })

        // Recycler
        binding.camListRecyclerTextImage.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.camListRecyclerTextImage.adapter = adapterCameraList

        //DecoratedBarcodeView
        intent.getParcelableArrayListExtra<MBarcodeFormat>("KEY_LIST_FORMAT")?.let {
            val barFormats = formatConverter(it)
            binding.camListDecorBarView.initializeFromIntent(intent)
            binding.camListDecorBarView.barcodeView.decoderFactory = DefaultDecoderFactory(barFormats)
            binding.camListDecorBarView.decodeContinuous(barcodeCallback)
        }

        binding.camListDecorBarView.setTorchListener(torchListener)
        binding.camListBtnTorch.setOnClickListener(btnTorchListener)

        if (!hasFlash()) binding.camListBtnTorch.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        binding.camListDecorBarView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.camListDecorBarView.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.camListDecorBarView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    // Кнопка "Назад" на ActionBar(toolbar)
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    // Аппаратная кнопка "Назад"
    override fun onBackPressed() {
        sendCancel()
        super.onBackPressed()
    }

    // Установка верхнего меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_camera_list, menu)
        return true
    }

    // Обработка нажатия кнопок верхнего меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.camListSave -> {
                sendSave(listScan)
                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    private val barcodeCallback = BarcodeCallback { result ->
        try {
            val scancode = result.text
            if (!scancode.equals(lastScancode)) {
                beepManager.playBeepSound()
                lastScancode = scancode
                binding.camListTextPrompt.text = scancode
                itemAdd(scancode, result.barcodeFormat.name, result.getBitmapWithResultPoints(Color.YELLOW))
            }
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.barcodeCallback: "+e.message) {}
        }
    }

    private fun itemAdd(scancode: String, format: String, bitmap: Bitmap) {
        try {
            listScan.add(MScan(scancode, format))
            listScanContinuous.add(MScanContinuous(scancode, bitmap))

            adapterCameraList.notifyItemInserted(listScanContinuous.size-1)
            binding.camListRecyclerTextImage.smoothScrollToPosition(listScanContinuous.size-1)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.itemAdd: "+e.message) {}
        }
    }

    private fun itemRemove(position: Int) {
        try {
            binding.camListTextPrompt.text = ""
            listScan.removeAt(position)
            listScanContinuous.removeAt(position)
            adapterCameraList.notifyItemRemoved(position)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.itemRemove: "+e.message) {}
        }
    }

    private fun sendSave(listScan: ArrayList<MScan>) {
        try {
            val intent = Intent()
            intent.putParcelableArrayListExtra("KEY_LIST_SCAN", listScan)
            setResult(RESULT_OK, intent)
            finish()
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.sendSave: "+e.message) {}
        }
    }

    private fun sendCancel() {
        try {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.sendCancel: "+e.message) {}
        }
    }

    private val torchListener = object: DecoratedBarcodeView.TorchListener {
        override fun onTorchOn() {
            binding.camListBtnTorch.setImageResource(R.drawable.img_torch_off_white_40)
        }
        override fun onTorchOff() {
            binding.camListBtnTorch.setImageResource(R.drawable.img_torch_on_white_40)
        }
    }

    private val btnTorchListener = View.OnClickListener {
        flagTorch = if (!flagTorch) {
            binding.camListDecorBarView.setTorchOn()
            true
        } else {
            binding.camListDecorBarView.setTorchOff()
            false
        }
    }

    private fun hasFlash(): Boolean {
        return try {
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        } catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.hasFlash: "+e.message) {}
            return false
        }
    }

    private fun formatConverter(listMBF: ArrayList<MBarcodeFormat>): Collection<BarcodeFormat> {
        return try {
            val listBF = mutableListOf<BarcodeFormat>()
            listMBF.forEach {
                listBF.add(it.barcodeFormat)
            }
            listBF
        } catch (e: Exception) {
            e.printStackTrace()
            showError(this, resources.getString(R.string.error_in)+
                    " ActivityCameraList.formatConverter: "+e.message) {}
            emptyList()
        }
    }
}

//    // Фотосканер Список
//    private fun camListStart() {
//        try {
//            if (!App.getAppContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
//                showInfo(weakContext.get()!!, resources.getString(R.string.attention_cameras)) {}
//                return
//            }
//
//            val intent = Intent(this, ActivityCameraList::class.java)
//            val listFormats = ArrayList<MBarcodeFormat>()
////            listFormats.add(MBarcodeFormat(BarcodeFormat.EAN_13))
////            listFormats.add(MBarcodeFormat(BarcodeFormat.EAN_8))
////            listFormats.add(MBarcodeFormat(BarcodeFormat.UPC_A))
////            listFormats.add(MBarcodeFormat(BarcodeFormat.UPC_E))
//            intent.putParcelableArrayListExtra("KEY_LIST_FORMAT", listFormats)
//            camListLauncher.launch(intent)
//        }catch (e: Exception) {
//            e.printStackTrace()
//            showError(this, resources.getString(R.string.error_in)+" _________.camListStart: "+e.message) {}
//        }
//    }
//
//    // Фотосканер Список результат
//    private val camListLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        try {
//            if (it.resultCode == Activity.RESULT_OK) {
//                it.data?.getParcelableArrayListExtra<MScan>("KEY_LIST_SCAN")?.let { listScan ->
//                    if (listScan.size > 0) {
//                        var mes = ""
//                        listScan.forEach { scan ->
//                            mes += "Scancode: ${scan.scancode} Format: ${scan.format}\n"
//                        }
//                        Toast.makeText(this, mes, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }catch (e: Exception) {
//            e.printStackTrace()
//            showError(this, resources.getString(R.string.error_in)+" _________.camListLauncher: "+e.message) {}
//        }
//    }