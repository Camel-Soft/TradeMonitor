package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods_detail

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._presentation.models.price.MPriceGoods
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.HoneywellEDA50K
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.honeyScanProp1D
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor._presentation.utils.scan.barcodeAutoCorrection
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor._presentation.utils.scan.pickBarcodeType
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.FragmentPriceGoodsDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPriceGoodsDetail : Fragment() {
    private lateinit var binding: FragmentPriceGoodsDetailBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var weakView: WeakReference<View>
    private lateinit var weakActivity: WeakReference<AppCompatActivity>
    private var argPriceGoods: MPriceGoods? = null
    private var argPriceColl: MPriceColl? = null
    @Inject lateinit var settings: Settings
    private lateinit var honeywellEDA50K: HoneywellEDA50K
    private val viewModel: FragmentPriceGoodsDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceGoodsDetailBinding.inflate(inflater)
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

        argPriceColl = arguments?.getParcelable("parentPriceColl")

        // Если null - Добавление нового ВРУЧНУЮ и Insert
        // Если не null - Обновление существующего и Update
        argPriceGoods = arguments?.getParcelable("priceGoods")
        if (argPriceGoods == null) {
            // Новый, ВРУЧНУЮ -> Insert
            prepInsert()
        }
        else {
            // Обновление существующего -> Update
            prepUpdate()
            fillFieldsUpdate()
        }

        // Сохранить
        binding.cardSave.setOnClickListener {
            if (argPriceGoods == null) {
                // Insert (добавление вручную) ****************************************************
                if (argPriceColl != null) {
                    val missFields = checkFillFields()
                    if (missFields.isEmpty()) {
                        val bundle = Bundle()
                        bundle.putParcelable("priceGoods", collectForInsert(argPriceColl!!))
                        setFragmentResult("DetailPriceGoods_Insert", bundle)
                        findNavController().popBackStack()
                    }
                    else
                        showInfo(weakContext.get()!!, resources.getString(R.string.need_fill_fields)+": $missFields") {}
                }
                else
                    showError(weakContext.get()!!, resources.getString(R.string.error_in)+
                            " FragmentPriceGoodsDetail.cardSave: "+resources.getString(R.string.error_parent_coll)) {}
            }
            else {
                // Update *************************************************************************
                val missFields = checkFillFields()
                if (missFields.isEmpty()) {
                    val bundle = Bundle()
                    bundle.putParcelable("priceGoods", collectForUpdate(argPriceGoods!!))
                    setFragmentResult("DetailPriceGoods_Update", bundle)
                    findNavController().popBackStack()
                }
                else
                    showInfo(weakContext.get()!!, resources.getString(R.string.need_fill_fields)+": $missFields") {}
            }
        }

        // Вешаем листенер для refresh-slide
        binding.refreshLayout.setOnRefreshListener {
            if (binding.editScan.text.toString().trim().count() in 6..13) {
                viewModel.eventsVm(EventsVmPriceGoodsDetail.SendRequestGoods(mGoodsBig = MGoodsBig(scancod = binding.editScan.text.toString().trim())))
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
                    is EventsUiPriceGoodsDetail.ShowError -> showError(weakContext.get()!!, event.message) {}
                    is EventsUiPriceGoodsDetail.ShowInfo -> showInfo(weakContext.get()!!, event.message) {}
                    is EventsUiPriceGoodsDetail.Progress -> {
                        if (event.show) binding.refreshLayout.isRefreshing = true
                        else binding.refreshLayout.isRefreshing = false
                    }
                    is EventsUiPriceGoodsDetail.Update -> Toast.makeText(weakContext.get()!!, event.message, Toast.LENGTH_SHORT).show()
                    is EventsUiPriceGoodsDetail.UnSuccess -> Toast.makeText(weakContext.get()!!, event.message, Toast.LENGTH_SHORT).show()
                    is EventsUiPriceGoodsDetail.Success -> {
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

    private val resultScanImpl: IResultScan = object : IResultScan {
        override fun actionScan(scan: EventsSync<MScan>) {
            try {
                when(scan) {
                    is EventsSync.Success -> {
                        if (argPriceGoods == null) {
                            binding.editScan.setText(scan.data.scancode)
                            binding.layoutScan.helperText = scan.data.format
                        }
                    }
                    is EventsSync.Error -> {
                        showError(weakContext.get()!!, scan.message) {}
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                showError(weakContext.get()!!, resources.getString(R.string.error_in)+" FragmentPriceGoodsDetail.resultScanImpl: "+e.message) {}
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
            layoutScan.helperText = ""
            editScan.setText("")
            editScan.isEnabled = false
            editQuantity.setText("")
            editEdIzm.setText("")
            editCena.setText("")
            editName.setText("")
            editNote.setText("")
        }
    }

    private fun prepInsert() {
        binding.apply {
            textTitle.text = getAppContext().resources.getString(R.string.insert_hand)
            textStatus.text = ""
            textStatus.setTextColor(getAppContext().getColor(R.color.black))
            textError.text = ""
            textError.setTextColor(getAppContext().getColor(R.color.black))
            layoutScan.helperText = ""
            editScan.setText("")
            editScan.isEnabled = true
            editQuantity.setText("")
            editEdIzm.setText("")
            editCena.setText("")
            editName.setText("")
            editNote.setText("")
        }
    }

    private fun fillFieldsUpdate() {
        binding.apply {
            argPriceGoods?.let {
                textStatus.text = getWrkMess(it.status_code).first
                textStatus.setTextColor(getAppContext().getColor(getWrkMess(it.status_code).second))
                textError.text = getErrMess(it.status_code).first
                textError.setTextColor(getAppContext().getColor(getErrMess(it.status_code).second))
                editScan.setText(it.scancode)
                layoutScan.helperText = getScanType(it.scancode_type)
                editQuantity.setText(toQuantity(it.quantity))
                editEdIzm.setText(it.ed_izm)
                editCena.setText(toMoney(it.cena))
                editName.setText(it.name)
                editNote.setText(it.note)
            }
        }
    }

    private fun checkFillFields(): String {
        var result = ""
        if (binding.editScan.text.toString().isEmpty()) {
            if (result.isEmpty()) result = getAppContext().resources.getString(R.string.scancode)
            else result += ", ${getAppContext().resources.getString(R.string.scancode)}"
        }
        return result
    }

    private fun collectForInsert(priceColl: MPriceColl): MPriceGoods {
        binding.apply {
            val barcode = if (settings.getAutoCorrBarcode()) barcodeAutoCorrection(editScan.text.toString())
            else editScan.text.toString()
            return MPriceGoods(
                id = 0L,
                id_coll = priceColl.id_coll,
                scancode = barcode,
                scancode_type = pickBarcodeType(barcode),
                cena = if (editCena.text.toString().isEmpty()) 0F else editCena.text.toString().toFloat(),
                note = editNote.text.toString(),
                name = editName.text.toString(),
                quantity = if (editQuantity.text.toString().isEmpty()) 0F else editQuantity.text.toString().toFloat(),
                ed_izm = editEdIzm.text.toString(),
                status_code = 0,
                holder_color = "1"
            )
        }
    }

    private fun collectForUpdate(basePriceGoods: MPriceGoods): MPriceGoods {
        binding.apply {
            return MPriceGoods(
                id = basePriceGoods.id,
                id_coll = basePriceGoods.id_coll,
                scancode = basePriceGoods.scancode,
                scancode_type = basePriceGoods.scancode_type,
                cena = if (editCena.text.toString().isEmpty()) 0F else editCena.text.toString().toFloat(),
                note = editNote.text.toString(),
                name = editName.text.toString(),
                quantity = if (editQuantity.text.toString().isEmpty()) 0F else editQuantity.text.toString().toFloat(),
                ed_izm = editEdIzm.text.toString(),
                status_code = 0,
                holder_color = basePriceGoods.holder_color
            )
        }
    }
}