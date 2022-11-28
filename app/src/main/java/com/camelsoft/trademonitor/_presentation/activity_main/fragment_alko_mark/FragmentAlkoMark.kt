package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.api.IResultScan
import com.camelsoft.trademonitor._presentation.api.IResultScanList
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera.ActivityCamera
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.ActivityCameraList
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models.MBarcodeFormat
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.HoneywellEDA50K
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.honeyScanProp2D
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.dialogs.showConfirm
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.utils.hideKeyboard
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.FragmentAlkoMarkBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentAlkoMark : Fragment() {

    private lateinit var binding: FragmentAlkoMarkBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var weakView: WeakReference<View>
    private lateinit var weakActivity: WeakReference<AppCompatActivity>
    private val viewModel: FragmentAlkoMarkViewModel by viewModels()
    private lateinit var parentAlkoColl: MAlkoColl
    @Inject lateinit var settings: Settings
    private lateinit var honeywellEDA50K: HoneywellEDA50K
    private val adapterAlkoMark = FragmentAlkoMarkAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlkoMarkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
        weakView = WeakReference<View>(view)
        weakActivity = WeakReference<AppCompatActivity>(requireActivity() as AppCompatActivity)

        if (settings.getScanner() == "honeywell_eda50k")
            honeywellEDA50K = HoneywellEDA50K(weakContext.get()!!, resultScanImpl, honeyScanProp2D())

        // Забираем данные о родительской сборке в переменную и проверяем на null
        // Если null, то показываем сообщение об ошибке и выходим
        val argAlkoColl: MAlkoColl? = arguments?.getParcelable("alkoColl")
        if (argAlkoColl == null) {
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+
                    " FragmentAlkoMark.onViewCreated: "+resources.getString(R.string.error_parent_coll))
            { findNavController().popBackStack() }
        }
        else {
            parentAlkoColl = argAlkoColl

            // Устанавливаем заголовок
            weakActivity.get()!!.supportActionBar?.title = parentAlkoColl.note

            // Клавиатура поверх/не поверх View (Совместно с манифестом. Устанавливать в каждом фрагменте)
            // Клавиатура поверх
            //weakActivity.get()!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            // Клавиатура не поверх
            //weakActivity.get()!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            // в манифесте для активити
            // android:windowSoftInputMode="adjustResize"

            // Меню
            val menuHost: MenuHost = weakActivity.get()!!
            menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)

            adapterListeners()
            binding.rvMarks.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL,false)
            binding.rvMarks.adapter = adapterAlkoMark
            viewModel.listAlkoMark.observe(viewLifecycleOwner) { adapterAlkoMark.submitList(it) }
            viewModel.onEventAlkoMark(EventVmAlkoMark.OnGetAlkoMarks(parentAlkoColl))

            // Фотосканер одиночный
            binding.btnScan.setOnClickListener { camStart() }
            // Фотосканер Список
            binding.btnScanList.setOnClickListener { camListStart() }

            eventsUiCollector()
            fragmentResultListener()
        }
    }

    override fun onResume() {
        super.onResume()
        if (settings.getScanner() == "honeywell_eda50k") honeywellEDA50K.reg()
        viewModel.onEventAlkoMark(EventVmAlkoMark.OnPublishPrice)
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

    // Меню - Создание, Обработка нажатий
    private val menuProvider: MenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_fragment_alko_mark, menu)
            val searchView = menu.findItem(R.id.btnSearch).actionView as SearchView
            searchView.queryHint = resources.getString(R.string.search_view_hint)
            searchView.setOnQueryTextListener(searchListener)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }

    // Поисковый листенер для меню
    private val searchListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            adapterAlkoMark.filter.filter(newText)
            return false
        }
    }

    // Адаптер - Листенеры нажатий
    private fun adapterListeners() {
        // Обновление товара
        adapterAlkoMark.setOnItemClickListener = { pos ->
            val bundle = Bundle()
            bundle.putParcelable("alkoMark", adapterAlkoMark.getList()[pos])
            findNavController().navigate(R.id.action_fragGraphAlkoMark_to_fragGraphAlkoMarkDetail, bundle)
        }

        // Удаление товара
        adapterAlkoMark.setOnItemLongClickListener = { pos ->
            showConfirm(
                context = weakContext.get()!!,
                title = resources.getString(R.string.marka_del_title),
                message = "${resources.getString(R.string.marka_del_message)}?"
            )
            { viewModel.onEventAlkoMark(EventVmAlkoMark.OnDeleteAlkoMark(parentAlkoColl, pos)) }
        }
    }

    // Ловим Update от detail-фрагмента (обновление выбранной позиции)
    private fun fragmentResultListener() {
        setFragmentResultListener("DetailAlkoMark_Update") { key, bundle ->
            val updAlkoMark: MAlkoMark? = bundle.getParcelable("alkoMark")
            updAlkoMark?.let {
                viewModel.onEventAlkoMark(EventVmAlkoMark.OnUpdateAlkoMark(parentAlkoColl = parentAlkoColl, alkoMark = it))
            }
        }
    }

    // Обработка событий Пользовательского Интерфейса
    private fun eventsUiCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUiAlkoMark.collect { eventUiAlkoMark ->
                when(eventUiAlkoMark) {
                    is EventUiAlkoMark.ShowErrorUi -> { showError(weakContext.get()!!, eventUiAlkoMark.message) {} }
                    is EventUiAlkoMark.ScrollToPos -> { binding.rvMarks.scrollToPosition(eventUiAlkoMark.position) }
                    is EventUiAlkoMark.PublishPrice -> {
                        eventUiAlkoMark.price?.let {
                            binding.chipPrc.text = it
                            binding.chipPrc.setTextColor(eventUiAlkoMark.color)
                            binding.chipPrc.visibility = View.VISIBLE
                        }?: binding.chipPrc.setTextColor(eventUiAlkoMark.color)
                    }
                }
            }
        }
    }

    // Фотосканер одиночный
    private fun camStart() {
        try {
            if (!App.getAppContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                showInfo(weakContext.get()!!, resources.getString(R.string.attention_cameras)) {}
                return
            }

            val scanOptions = ScanOptions()
            scanOptions.captureActivity = ActivityCamera::class.java
            scanOptions.setDesiredBarcodeFormats(
                ScanOptions.DATA_MATRIX,
                ScanOptions.PDF_417,
                ScanOptions.QR_CODE)
            scanOptions.setCameraId(0)
            scanOptions.setBeepEnabled(true)
            scanOptions.setPrompt("")
            camLauncher.launch(scanOptions)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMark.camStart: "+e.message) {}
        }
    }

    // Фотосканер одиночный
    private val camLauncher = registerForActivityResult( ScanContract() ) {
        try {
            if (it.contents != null && it.formatName != null)
                resultScanImpl.actionScan(EventsSync.Success(MScan(scancode = it.contents, format = it.formatName)))
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMark.camLauncher: "+e.message) {}
        }
    }

    // Одиночное сканирование. Обработка (Фотосканер одиночный, HoneywellEDA50K, ...)
    private val resultScanImpl: IResultScan = object : IResultScan {
        override fun actionScan(scan: EventsSync<MScan>) {
            try {
                when(scan) {
                    is EventsSync.Success -> {
                        viewModel.onEventAlkoMark(EventVmAlkoMark.OnInsertOrUpdateAlkoMark(parentAlkoColl, scan.data))
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scan.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMark.resultScanImpl: "+e.message) {}
            }
        }
    }

    // Фотосканер Список
    private fun camListStart() {
        try {
            if (!App.getAppContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                showInfo(weakContext.get()!!, resources.getString(R.string.attention_cameras)) {}
                return
            }

            val intent = Intent(weakContext.get()!!, ActivityCameraList::class.java)
            val listFormats = ArrayList<MBarcodeFormat>()
            listFormats.add(MBarcodeFormat(BarcodeFormat.DATA_MATRIX))
            listFormats.add(MBarcodeFormat(BarcodeFormat.PDF_417))
            listFormats.add(MBarcodeFormat(BarcodeFormat.QR_CODE))
            intent.putParcelableArrayListExtra("KEY_LIST_FORMAT", listFormats)
            camListLauncher.launch(intent)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMark.camListStart: "+e.message) {}
        }
    }

    // Фотосканер Список
    private val camListLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        try {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.getParcelableArrayListExtra<MScan>("KEY_LIST_SCAN")?.let {
                    if (it.size > 0) { resultScanListImpl.actionScanList(EventsSync.Success(it)) }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMark.camListLauncher: "+e.message) {}
        }
    }

    // Списочное сканирование. Обработка (Фотосканер Список, ...)
    private val resultScanListImpl: IResultScanList = object : IResultScanList {
        override fun actionScanList(scanList: EventsSync<ArrayList<MScan>>) {
            try {
                when(scanList) {
                    is EventsSync.Success -> {
                        viewModel.onEventAlkoMark(EventVmAlkoMark.OnInsertOrUpdateAlkoMarks(parentAlkoColl, scanList.data))
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scanList.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentAlkoMark.resultScanListImpl: "+e.message) {}
            }
        }
    }
}
