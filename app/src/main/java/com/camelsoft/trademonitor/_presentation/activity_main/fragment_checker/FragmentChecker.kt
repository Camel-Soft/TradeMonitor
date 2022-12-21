package com.camelsoft.trademonitor._presentation.activity_main.fragment_checker

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera.ActivityCamera
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.HoneywellEDA50K
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.honeyScanPropChecker
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.hideKeyboard
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.FragmentCheckerBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentChecker : Fragment() {
    private lateinit var binding: FragmentCheckerBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var weakView: WeakReference<View>
    private lateinit var weakActivity: WeakReference<AppCompatActivity>
    @Inject lateinit var settings: Settings
    private lateinit var honeywellEDA50K: HoneywellEDA50K
    private val viewModel: FragmentCheckerViewModel by viewModels()
    private val fragmentCheckerAdapter = FragmentCheckerAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
        weakView = WeakReference<View>(view)
        weakActivity = WeakReference<AppCompatActivity>(requireActivity() as AppCompatActivity)

        if (settings.getScanner() == "honeywell_eda50k")
            honeywellEDA50K = HoneywellEDA50K(weakContext.get()!!, resultScanImpl, honeyScanPropChecker())

        // Фотосканер одиночный
        binding.btnScan.setOnClickListener { camStart() }

        eventsUiCollector()
        rvSetter()
        btnSearchListeners()
    }

    override fun onResume() {
        super.onResume()
        if (settings.getScanner() == "honeywell_eda50k") honeywellEDA50K.reg()
        // Клавиатура поверх
        weakActivity.get()!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onPause() {
        super.onPause()
        if (settings.getScanner() == "honeywell_eda50k") honeywellEDA50K.unreg()
        hideKeyboard(weakContext.get()!!, weakView.get())
        // Клавиатура не поверх
        weakActivity.get()!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    // Кнопка Ручного поиска - Листенер
    private fun btnSearchListeners() {
        binding.btnSearch.setOnClickListener {
            if (binding.editScan.text.toString().trim().length > 1)
                viewModel.eventsVm(EventsVmChecker.PerformRequest(mScan = MScan(
                    scancode = binding.editScan.text.toString().trim(),
                    format = "HAND_MADE"
                )))
            else showInfo(weakContext.get()!!, resources.getString(R.string.info_length_2)) {}
        }
    }

    // Установки RecyclerView - Список с данными о товаре
    private fun rvSetter() {
        binding.rv.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL,false)
        binding.rv.adapter = fragmentCheckerAdapter
        viewModel.listMStringString.observe(viewLifecycleOwner) {
            fragmentCheckerAdapter.submitList(it)
        }
    }

    // Обработка событий Пользовательского Интерфейса
    private fun eventsUiCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsUi.collect { eventUi ->
                when(eventUi) {
                    is EventsUiChecker.ShowError -> showError(weakContext.get()!!, eventUi.message) {}
                    is EventsUiChecker.ShowInfo -> showInfo(weakContext.get()!!, eventUi.message) {}
                    is EventsUiChecker.ShowToast -> Toast.makeText(weakContext.get()!!, eventUi.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Фотосканер одиночный
    private fun camStart() {
        try {
            if (!getAppContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                showInfo(weakContext.get()!!, resources.getString(R.string.attention_cameras)) {}
                return
            }

            val scanOptions = ScanOptions()
            scanOptions.captureActivity = ActivityCamera::class.java
            scanOptions.setDesiredBarcodeFormats(
                ScanOptions.EAN_13,
                ScanOptions.EAN_8,
                ScanOptions.UPC_A,
                ScanOptions.UPC_E,
                ScanOptions.DATA_MATRIX
            )
            scanOptions.setCameraId(0)
            scanOptions.setBeepEnabled(true)
            scanOptions.setPrompt("")
            camLauncher.launch(scanOptions)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!,"[FragmentChecker.camStart] ${e.localizedMessage}") {}
        }
    }

    // Фотосканер одиночный
    private val camLauncher = registerForActivityResult( ScanContract() ) {
        try {
            if (it.contents != null && it.formatName != null)
                resultScanImpl.actionScan(EventsSync.Success(MScan(scancode = it.contents, format = it.formatName)))
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!,"[FragmentChecker.camLauncher] ${e.localizedMessage}") {}
        }
    }

    // Одиночное сканирование. Обработка (Фотосканер одиночный, HoneywellEDA50K, ...)
    private val resultScanImpl: IResultScan = object : IResultScan {
        override fun actionScan(scan: EventsSync<MScan>) {
            try {
                when(scan) {
                    is EventsSync.Success -> {
                        binding.editScan.setText(scan.data.scancode)
                        viewModel.eventsVm(EventsVmChecker.PerformRequest(mScan = scan.data))
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scan.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!,"[FragmentChecker.resultScanImpl] ${e.localizedMessage}") {}
            }
        }
    }
}
