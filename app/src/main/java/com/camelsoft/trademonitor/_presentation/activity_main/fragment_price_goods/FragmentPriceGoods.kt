package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

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
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._presentation.models.price.MPriceGoods
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.api.scan.IResultScanList
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.HoneywellEDA50K
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera.ActivityCamera
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.ActivityCameraList
import com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list.models.MBarcodeFormat
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.honeyScanProp1D
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.dialogs.showConfirm
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.utils.hideKeyboard
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.FragmentPriceGoodsBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPriceGoods : Fragment() {

    private lateinit var binding: FragmentPriceGoodsBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var weakView: WeakReference<View>
    private lateinit var weakActivity: WeakReference<AppCompatActivity>
    private val viewModel: FragmentPriceGoodsViewModel by viewModels()
    private lateinit var parentPriceColl: MPriceColl
    @Inject lateinit var settings: Settings
    private lateinit var honeywellEDA50K: HoneywellEDA50K
    private val adapterGoods = FragmentPriceGoodsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceGoodsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
        weakView = WeakReference<View>(view)
        weakActivity = WeakReference<AppCompatActivity>(requireActivity() as AppCompatActivity)

        if (settings.getScanner() == "honeywell_eda50k")
            honeywellEDA50K = HoneywellEDA50K(weakContext.get()!!, resultScanImpl, honeyScanProp1D())

        // Забираем данные о родительской сборке в переменную и проверяем на null
        // Если null, то показываем сообщение об ошибке и выходим
        val argPriceColl: MPriceColl? = arguments?.getParcelable("priceColl")
        if (argPriceColl == null) {
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+
                    " FragmentPriceGoods.onViewCreated: "+resources.getString(R.string.error_parent_coll))
            { findNavController().popBackStack() }
        }
        else {
            parentPriceColl = argPriceColl

            // Устанавливаем заголовок
            weakActivity.get()!!.supportActionBar?.title = parentPriceColl.note

            // Меню
            val menuHost: MenuHost = weakActivity.get()!!
            menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)

            adapterListeners()
            binding.rvGoods.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL,false)
            binding.rvGoods.adapter = adapterGoods
            viewModel.listPriceGoods.observe(viewLifecycleOwner) { adapterGoods.submitList(it) }
            viewModel.onEventGoods(EventVmGoods.OnGetGoodes(parentPriceColl))

            // Фотосканер одиночный
            binding.btnScan.setOnClickListener { camStart() }

            // Фотосканер Список
            binding.btnScanList.setOnClickListener { camListStart() }

            eventsUiCollector()
            fragmentResultListeners()
            btnHandListener()
        }
    }

    override fun onResume() {
        super.onResume()
        if (settings.getScanner() == "honeywell_eda50k") honeywellEDA50K.reg()
        viewModel.onEventGoods(EventVmGoods.OnPublishPrice)
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
            menuInflater.inflate(R.menu.menu_fragment_price_goods, menu)
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
            adapterGoods.filter.filter(newText)
            return false
        }
    }

    // Кнопка - Добавление вручную
    private fun btnHandListener() {
        binding.btnHand.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("parentPriceColl", parentPriceColl)
            findNavController().navigate(R.id.action_fragGraphPriceGoods_to_fragGraphPriceGoodsDetail, bundle)
        }
    }

    // Адаптер - Листенеры нажатий
    private fun adapterListeners() {
        // Нажатие - Обновление товара
        adapterGoods.setOnItemClickListener = { pos ->
            val bundle = Bundle()
            bundle.putParcelable("priceGoods", adapterGoods.getList()[pos])
            findNavController().navigate(R.id.action_fragGraphPriceGoods_to_fragGraphPriceGoodsDetail, bundle)
        }
        // Нажатие - Удаление товара
        adapterGoods.setOnItemLongClickListener = { pos ->
            showConfirm(
                context = weakContext.get()!!,
                title = resources.getString(R.string.goods_del_title),
                message = resources.getString(R.string.goods_del_message)+": ${adapterGoods.getList()[pos].scancode} ?"
            )
            { viewModel.onEventGoods(EventVmGoods.OnDeleteGoods(parentPriceColl, pos)) }
        }
    }

    // Result - Listeners
    private fun fragmentResultListeners() {
        // Ловим Insert от detail-фрагмента (добавление вручную)
        setFragmentResultListener("DetailPriceGoods_Insert") { key, bundle ->
            val insPriceGoods: MPriceGoods? = bundle.getParcelable("priceGoods")
            insPriceGoods?.let {
                viewModel.onEventGoods(EventVmGoods.OnInsertOrUpdateGoodsHandmade(parentColl = parentPriceColl, priceGoods = it))

            }
        }

        // Ловим Update от detail-фрагмента (обновление выбранной позиции)
        setFragmentResultListener("DetailPriceGoods_Update") { key, bundle ->
            val updPriceGoods: MPriceGoods? = bundle.getParcelable("priceGoods")
            updPriceGoods?.let {
                viewModel.onEventGoods(EventVmGoods.OnUpdateGoods(parentColl = parentPriceColl, priceGoods = it))
            }
        }
    }

    // Обработка событий Пользовательского Интерфейса
    private fun eventsUiCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUiGoods.collect { eventUiGoods ->
                when(eventUiGoods) {
                    is EventUiGoods.ShowErrorUi -> { showError(weakContext.get()!!, eventUiGoods.message) {} }
                    is EventUiGoods.ScrollToPos -> { binding.rvGoods.scrollToPosition(eventUiGoods.position) }
                    is EventUiGoods.PublishPrice -> {
                        eventUiGoods.price?.let {
                            binding.chipPrc.text = it
                            binding.chipPrc.setTextColor(eventUiGoods.color)
                            binding.chipPrc.visibility = View.VISIBLE
                        }?: binding.chipPrc.setTextColor(eventUiGoods.color)
                    }
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
                ScanOptions.UPC_E)
            scanOptions.setCameraId(0)
            scanOptions.setBeepEnabled(true)
            scanOptions.setPrompt("")
            camLauncher.launch(scanOptions)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoods.camStart: "+e.message) {}
        }
    }

    // Фотосканер одиночный
    private val camLauncher = registerForActivityResult( ScanContract() ) {
        try {
            if (it.contents != null && it.formatName != null)
                resultScanImpl.actionScan(EventsSync.Success(MScan(scancode = it.contents, format = it.formatName)))
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoods.camLauncher: "+e.message) {}
        }
    }

    // Одиночное сканирование. Обработка (Фотосканер одиночный, HoneywellEDA50K, ...)
    private val resultScanImpl: IResultScan = object : IResultScan {
        override fun actionScan(scan: EventsSync<MScan>) {
            try {
                when(scan) {
                    is EventsSync.Success -> {
                        viewModel.onEventGoods(EventVmGoods.OnInsertOrUpdateGoods(parentPriceColl, scan.data))
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scan.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoods.resultScanImpl: "+e.message) {}
            }
        }
    }

    // Фотосканер Список
    private fun camListStart() {
        try {
            if (!getAppContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                showInfo(weakContext.get()!!, resources.getString(R.string.attention_cameras)) {}
                return
            }

            val intent = Intent(weakContext.get()!!, ActivityCameraList::class.java)
            val listFormats = ArrayList<MBarcodeFormat>()
            listFormats.add(MBarcodeFormat(BarcodeFormat.EAN_13))
            listFormats.add(MBarcodeFormat(BarcodeFormat.EAN_8))
            listFormats.add(MBarcodeFormat(BarcodeFormat.UPC_A))
            listFormats.add(MBarcodeFormat(BarcodeFormat.UPC_E))
            intent.putParcelableArrayListExtra("KEY_LIST_FORMAT", listFormats)
            camListLauncher.launch(intent)
        }catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoods.camListStart: "+e.message) {}
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
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoods.camListLauncher: "+e.message) {}
        }
    }

    // Списочное сканирование. Обработка (Фотосканер Список, ...)
    private val resultScanListImpl: IResultScanList = object : IResultScanList {
        override fun actionScanList(scanList: EventsSync<ArrayList<MScan>>) {
            try {
                when(scanList) {
                    is EventsSync.Success -> {
                        viewModel.onEventGoods(EventVmGoods.OnInsertOrUpdateGoodes(parentPriceColl, scanList.data))
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scanList.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoods.resultScanListImpl: "+e.message) {}
            }
        }
    }
}