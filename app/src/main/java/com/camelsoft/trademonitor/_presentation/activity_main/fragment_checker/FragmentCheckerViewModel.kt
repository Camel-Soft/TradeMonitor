package com.camelsoft.trademonitor._presentation.activity_main.fragment_checker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.api.IGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.models.secondary.MStringString
import com.camelsoft.trademonitor._presentation.utils.prcDateReturn
import com.camelsoft.trademonitor._presentation.utils.scan.checkBarcode
import com.camelsoft.trademonitor._presentation.utils.scan.getScanFromDataMatrix
import com.camelsoft.trademonitor._presentation.utils.scan.pickBarcodeType
import com.camelsoft.trademonitor._presentation.utils.toQuantity
import com.camelsoft.trademonitor._presentation.utils.toSouthCena
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentCheckerViewModel @Inject constructor(
    private val settings: Settings,
    private val iGoods: IGoods
): ViewModel() {

    private val _eventsUi = Channel<EventsUiChecker>()
    val eventsUi = _eventsUi.receiveAsFlow()

    private val _listMStringString = MutableLiveData<List<MStringString>>()
    val listMStringString: LiveData<List<MStringString>> = _listMStringString

    fun eventsVm(event: EventsVmChecker) {
        when (event) {
            is EventsVmChecker.PerformRequest -> {
                viewModelScope.launch(performRequestEH) {
                    _listMStringString.value = emptyList()
                    val mGoodsBig: MGoodsBig? = makeMGoodsBig(mScan = event.mScan)
                    mGoodsBig?.let {
                        when (val result = iGoods.getGoodsBig(mGoodsBig = it)) {
                            is EventsGoods.Success -> _listMStringString.value = makeListMStringString(mGoodsBig = result.data)
                            is EventsGoods.UnSuccess -> sendEventUi(EventsUiChecker.ShowToast(message = getAppContext().resources.getString(R.string.goods_not_found)))
                            is EventsGoods.Update -> sendEventUi(EventsUiChecker.ShowToast(message = getAppContext().resources.getString(R.string.database_update)))
                            is EventsGoods.Info -> sendEventUi(EventsUiChecker.ShowInfo(message = result.message))
                            is EventsGoods.Error -> sendEventUi(EventsUiChecker.ShowError(message = result.message))
                        }
                    }?: sendEventUi(EventsUiChecker.ShowInfo(message = getAppContext().resources.getString(R.string.error_scancode_search)))
                }
            }
        }
    }

    private val performRequestEH = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        sendEventUi(EventsUiChecker.ShowError("[FragmentCheckerViewModel.PerformRequest] ${throwable.localizedMessage}"))
    }

    private fun makeListMStringString(mGoodsBig: MGoodsBig): List<MStringString> {
        try {
            val result = ArrayList<MStringString>()
            if (mGoodsBig.prc_number.isNotBlank() && mGoodsBig.prc_date.trim().length == 8)
                result.add(MStringString(getAppContext().resources.getString(R.string.price_list), "N ${mGoodsBig.prc_number} ${getAppContext().resources.getString(R.string.from)} ${prcDateReturn(mGoodsBig.prc_date)} ${mGoodsBig.prc_time}"))
            if (mGoodsBig.sklad.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_sklad), mGoodsBig.sklad))
            if (mGoodsBig.name.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_name), mGoodsBig.name))
            if (mGoodsBig.name_full.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_name_full), mGoodsBig.name_full))
            if (mGoodsBig.ed_izm.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_ed_izm), mGoodsBig.ed_izm))
            if (mGoodsBig.cena2.isNotBlank() || mGoodsBig.cena3.isNotBlank()) {
                if (mGoodsBig.cena1.isNotBlank()) result.add(MStringString("${getAppContext().resources.getString(R.string.price_cena)} 1", "${mGoodsBig.cena1.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
            }
            else {
                if (mGoodsBig.cena1.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_cena), "${mGoodsBig.cena1.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
            }
            if (mGoodsBig.cena2.isNotBlank()) result.add(MStringString("${getAppContext().resources.getString(R.string.price_cena)} 2","${mGoodsBig.cena2.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
            if (mGoodsBig.cena3.isNotBlank()) result.add(MStringString("${getAppContext().resources.getString(R.string.price_cena)} 3","${mGoodsBig.cena3.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
            if (mGoodsBig.articul.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_articul), mGoodsBig.articul))
            if (mGoodsBig.made.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_made), mGoodsBig.made))
            if (mGoodsBig.grt.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_grt), mGoodsBig.grt))
            if (mGoodsBig.sgrt.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_sgrt), mGoodsBig.sgrt))
            if (mGoodsBig.day_save.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_day_save), toQuantity(mGoodsBig.day_save)))
            if (mGoodsBig.in_place.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_in_place), toQuantity(mGoodsBig.in_place)))
            if (mGoodsBig.litera.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_litera), mGoodsBig.litera))
            if (mGoodsBig.prc_type.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_type), mGoodsBig.prc_type))
            if (mGoodsBig.nds.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_nds), "${mGoodsBig.nds} %"))
            if (mGoodsBig.mrc.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_mrc), "${mGoodsBig.mrc.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
            if (mGoodsBig.cod_vvp.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_cod_vvp), mGoodsBig.cod_vvp))
            if (mGoodsBig.capacity.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_capacity), toQuantity(mGoodsBig.capacity)))
            if (mGoodsBig.rezerv1.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_rezerv1), mGoodsBig.rezerv1))
            if (mGoodsBig.modelline.toBoolean() && mGoodsBig.nsp.isNotBlank() && mGoodsBig.nsp != "0") {
                when (mGoodsBig.nsp) {
                    "1" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), getAppContext().resources.getString(R.string.price_marker_a)))
                    "2" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), getAppContext().resources.getString(R.string.price_marker_b)))
                    "3" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), getAppContext().resources.getString(R.string.price_marker_c)))
                    "4" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), "${getAppContext().resources.getString(R.string.price_marker_d)} - ${mGoodsBig.cena_spec.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
                    "5" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), "${getAppContext().resources.getString(R.string.price_marker_e)} - ${mGoodsBig.cena_spec.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
                    "6" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), "${getAppContext().resources.getString(R.string.price_marker_f)} - ${mGoodsBig.cena_spec.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"))
                    "7" -> result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), getAppContext().resources.getString(R.string.price_marker_g)))
                    "8" -> {
                        if (mGoodsBig.cena2.isBlank() && mGoodsBig.cena3.isBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), getAppContext().resources.getString(R.string.price_marker_r)))
                        if (mGoodsBig.cena2 == "0" && mGoodsBig.cena3 == "0") result.add(MStringString("${getAppContext().resources.getString(R.string.price_marker_title)} - ${getAppContext().resources.getString(R.string.price_marker_loc_title)}", getAppContext().resources.getString(R.string.price_marker_loc)))
                    }
                    "9" -> {
                        if (mGoodsBig.cena2.isBlank() && mGoodsBig.cena3.isBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_marker_title), getAppContext().resources.getString(R.string.price_marker_t)))
                        if (mGoodsBig.cena2.isNotBlank() && mGoodsBig.cena3.isNotBlank() && (mGoodsBig.cena2 != "0" || mGoodsBig.cena3 != "0")) {
                            var str = ""
                            if (mGoodsBig.cena2.isNotBlank() && mGoodsBig.cena2 != "0") str = "${mGoodsBig.cena2.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"
                            if (mGoodsBig.cena3.isNotBlank() && mGoodsBig.cena3 != "0") {
                                if (str.isBlank()) str = "${mGoodsBig.cena3.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}" else str += " ${getAppContext().resources.getString(R.string.and)} ${mGoodsBig.cena3.toSouthCena()} ${getAppContext().resources.getString(R.string.money)}"
                            }
                            if (str.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_additional_mrc), str))
                        }
                    }
                    else -> {}
                }
            }
            if (mGoodsBig.cod.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_cod), mGoodsBig.cod))
            if (mGoodsBig.scancod_is_find.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_scancod_is_find), mGoodsBig.scancod_is_find))
            if (mGoodsBig.scancod_type.isNotBlank()) result.add(MStringString(getAppContext().resources.getString(R.string.price_scancod_type), mGoodsBig.scancod_type))
            return result
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[FragmentCheckerViewModel.makeListMStringString] ${e.localizedMessage}")
        }
    }

    private fun makeMGoodsBig(mScan: MScan): MGoodsBig? {
        try {
            when (mScan.format) {
                "EAN_8", "UPC_A", "UPC_E" -> return MGoodsBig(scancod = mScan.scancode, scancod_type = mScan.format)
                "EAN_13" -> {
                    if (mScan.scancode.substring(0,2) != settings.getPrefix())
                        return MGoodsBig(scancod = mScan.scancode, scancod_type = mScan.format)
                    else
                        return MGoodsBig(scancod = mScan.scancode.substring(0,7), scancod_type = "EAN_13_WEIGHT")
                }
                "DATA_MATRIX" -> {
                    val pair = getScanFromDataMatrix(dataMatrix = mScan.scancode)
                    if (pair.first == "") {
                        return null
                    } else {
                        return MGoodsBig(
                            scancod = pair.first,
                            scancod_type = pair.second,
                            marka = mScan.scancode,
                            marka_type = mScan.format
                        )
                    }
                }
                "HAND_MADE" -> {
                    if (mScan.scancode.length >= 18) {
                        val pair = getScanFromDataMatrix(dataMatrix = mScan.scancode)
                        if (pair.first.isNotBlank()) return MGoodsBig(
                            scancod = pair.first,
                            scancod_type = pair.second,
                            marka = mScan.scancode,
                            marka_type = "DATA_MATRIX"
                        )
                    }
                    val scancodType = pickBarcodeType(barcode = mScan.scancode)
                    if (scancodType != "EAN_13_WEIGHT") return MGoodsBig(scancod = mScan.scancode, scancod_type = scancodType)
                    else
                        return MGoodsBig(scancod = mScan.scancode.substring(0,7), scancod_type = scancodType)
                }
                else -> return null
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception("[FragmentCheckerViewModel.makeMGoodsBig] ${e.localizedMessage}")
        }
    }

    private fun sendEventUi(event: EventsUiChecker) {
        viewModelScope.launch {
            _eventsUi.send(event)
        }
    }
}
