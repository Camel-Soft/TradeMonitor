package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark_detail

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera.ActivityCamera
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.HoneywellEDA50K
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.honeyScanProp1D
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.utils.scan.barcodeAutoCorrection
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor._presentation.utils.scan.pickBarcodeType
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.FragmentAlkoMarkDetailBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentAlkoMarkDetail : Fragment() {
    private lateinit var binding: FragmentAlkoMarkDetailBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var weakView: WeakReference<View>
    private lateinit var weakActivity: WeakReference<AppCompatActivity>
    private var argAlkoMark: MAlkoMark? = null
    @Inject lateinit var settings: Settings
    private lateinit var honeywellEDA50K: HoneywellEDA50K
    private val viewModel: FragmentAlkoMarkDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlkoMarkDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
        weakView = WeakReference<View>(view)
        weakActivity = WeakReference<AppCompatActivity>(requireActivity() as AppCompatActivity)

        // Устанавливаем заголовок
        weakActivity.get()!!.supportActionBar?.title = ""

        // Устанавливаем цвет верхней панели - белый
        weakActivity.get()!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(getAppContext().getColor(R.color.white)))

        // Встроенный сканер
        if (settings.getScanner() == "honeywell_eda50k")
            honeywellEDA50K = HoneywellEDA50K(weakContext.get()!!, resultScanImpl, honeyScanProp1D())

        // Если null - выход
        // Если не null - готовимся к Update
        argAlkoMark = arguments?.getParcelable("alkoMark")
        if (argAlkoMark == null) {
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+
                    " FragmentAlkoMarkDetail.onViewCreated: "+resources.getString(R.string.error_alko_mark))
            { findNavController().popBackStack() }
        }
        else {
            // готовимся к Update

            prepUpdate()
            fillFieldsUpdate()
            // Вешаем листенер для кнопки "Сохранить"
            binding.cardSave.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("alkoMark", collectForUpdate(argAlkoMark!!))
                setFragmentResult("DetailAlkoMark_Update", bundle)
                findNavController().popBackStack()
            }
            // Вешаем листенер для кнопки "Сканировать сканкод"
            binding.btnScan.setOnClickListener { camStart() }
            // Вешаем листенер для refresh-slide
            binding.refreshLayout.setOnRefreshListener {
                if (binding.editScan.text.toString().trim().count() in 6..13) {
                    viewModel.eventsVm(EventsVmAlkoMarkDetail.SendRequestGoods(mGoodsBig = MGoodsBig(scancod = binding.editScan.text.toString().trim())))
                }
                else {
                    binding.refreshLayout.isRefreshing = false
                    Toast.makeText(weakContext.get()!!, getAppContext().resources.getString(R.string.bad_scancod), Toast.LENGTH_SHORT).show()
                }
            }
            handleEventsUi()
            summListeners()
            summShow()
        }
    }

    // Авторасчет суммы (листенеры)
    private fun summListeners() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) { summShow() }
        }
        binding.editQuantity.addTextChangedListener(textWatcher)
        binding.editCena.addTextChangedListener(textWatcher)
    }

    // Авторасчет суммы (отображение)
    private fun summShow() {
        val summ = autoSumm(kolvo = binding.editQuantity.text.toString(), cena = binding.editCena.text.toString())
        if (summ.isNotBlank() && summ != "0") binding.layoutCena.helperText = "${resources.getString(R.string.summa)}: $summ" else binding.layoutCena.helperText = ""
    }

    // Обработка событий пользовательского интерфейса
    private fun handleEventsUi() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsUi.collect { event ->
                when (event) {
                    is EventsUiAlkoMarkDetail.ShowError -> showError(weakContext.get()!!, event.message) {}
                    is EventsUiAlkoMarkDetail.ShowInfo -> showInfo(weakContext.get()!!, event.message) {}
                    is EventsUiAlkoMarkDetail.Progress -> {
                        if (event.show) binding.refreshLayout.isRefreshing = true
                        else binding.refreshLayout.isRefreshing = false
                    }
                    is EventsUiAlkoMarkDetail.Update -> Toast.makeText(weakContext.get()!!, event.message, Toast.LENGTH_SHORT).show()
                    is EventsUiAlkoMarkDetail.UnSuccess -> Toast.makeText(weakContext.get()!!, event.message, Toast.LENGTH_SHORT).show()
                    is EventsUiAlkoMarkDetail.Success -> {
                        binding.editName.setText(event.mGoodsBig.name)
                        binding.editCena.setText(event.mGoodsBig.cena1.toSouthCena())
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (settings.getScanner() == "honeywell_eda50k") honeywellEDA50K.reg()
    }

    override fun onPause() {
        super.onPause()
        if (settings.getScanner() == "honeywell_eda50k") honeywellEDA50K.unreg()
        hideKeyboard(weakContext.get()!!, weakView.get())
    }

    override fun onDestroyView() {
        // Устанавливаем цвет верхней панели - возвращаем назад
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setBackgroundDrawable(ColorDrawable(getAppContext().getColor(R.color.yellow_200)))
        super.onDestroyView()
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
                ScanOptions.UPC_E)
            scanOptions.setCameraId(0)
            scanOptions.setBeepEnabled(true)
            scanOptions.setPrompt("")
            camLauncher.launch(scanOptions)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMarkDetail.camStart: "+e.message) {}
        }
    }

    // Фотосканер одиночный
    private val camLauncher = registerForActivityResult( ScanContract() ) {
        try {
            if (it.contents != null && it.formatName != null)
                resultScanImpl.actionScan(EventsSync.Success(MScan(scancode = it.contents, format = it.formatName)))
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMarkDetail.camLauncher: "+e.message) {}
        }
    }

    private val resultScanImpl: IResultScan = object : IResultScan {
        override fun actionScan(scan: EventsSync<MScan>) {
            try {
                when(scan) {
                    is EventsSync.Success -> {
                        binding.editScan.setText(scan.data.scancode)
                        binding.layoutScan.helperText = scan.data.format
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scan.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMarkDetail.resultScanImpl: "+e.message) {}
            }
        }
    }

    private fun prepUpdate() {
        binding.apply {
            textTitle.text = getAppContext().resources.getString(R.string.change)
            textStatus.text = ""
            textStatus.setTextColor(getAppContext().getColor(R.color.black))
            textError.text = ""
            textError.setTextColor(getAppContext().getColor(R.color.black))
            layoutMark.helperText = ""
            editMark.setText("")
            editMark.isEnabled = false
            layoutScan.helperText = ""
            editScan.setText("")
            editQuantity.setText("")
            editCena.setText("")
            editName.setText("")
            editNote.setText("")
        }
    }

    private fun fillFieldsUpdate() {
        binding.apply {
            argAlkoMark?.let {
                textStatus.text = getWrkMess(it.status_code).first
                textStatus.setTextColor(getAppContext().getColor(getWrkMess(it.status_code).second))
                textError.text = getErrMess(it.status_code).first
                textError.setTextColor(getAppContext().getColor(getErrMess(it.status_code).second))
                editMark.setText(it.marka)
                layoutMark.helperText = getScanType(it.marka_type)
                editScan.setText(it.scancode)
                layoutScan.helperText = getScanType(it.scancode_type)
                editQuantity.setText(toQuantity(it.quantity))
                editCena.setText(toMoney(it.cena))
                editName.setText(it.name)
                editNote.setText(it.note)
            }
        }
    }

    private fun collectForUpdate(baseAlkoMark: MAlkoMark): MAlkoMark {
        binding.apply {
            val barcode = if (settings.getAutoCorrBarcode()) barcodeAutoCorrection(editScan.text.toString())
            else editScan.text.toString()

            return MAlkoMark(
                id = baseAlkoMark.id,
                id_coll = baseAlkoMark.id_coll,
                marka = baseAlkoMark.marka,
                marka_type = baseAlkoMark.marka_type,
                scancode = barcode,
                scancode_type = if (barcode.isEmpty()) "" else pickBarcodeType(barcode),
                cena = if (editCena.text.toString().isEmpty()) 0F else editCena.text.toString().toFloat(),
                note = editNote.text.toString(),
                name = editName.text.toString(),
                quantity = if (editQuantity.text.toString().isEmpty()) 0F else editQuantity.text.toString().toFloat(),
                type = "",
                status_code = 0,
                holder_color = "white"
            )
        }
    }
}