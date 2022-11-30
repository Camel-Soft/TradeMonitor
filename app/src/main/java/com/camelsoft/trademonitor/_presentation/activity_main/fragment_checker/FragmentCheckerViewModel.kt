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
import com.camelsoft.trademonitor._presentation.utils.scan.checkBarcode
import com.camelsoft.trademonitor._presentation.utils.scan.getScanFromDataMatrix
import com.camelsoft.trademonitor._presentation.utils.scan.pickBarcodeType
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
                    val mGoodsBig: MGoodsBig? = makeMGoodsBig(mScan = event.mScan)
                    mGoodsBig?.let {
                        when (val result = iGoods.getGoodsBig(mGoodsBig = it)) {
                            is EventsGoods.Success -> {}
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
                    if (pair.first == "") return null else
                        return MGoodsBig(
                            scancod = pair.first,
                            scancod_type = pair.second,
                            marka = mScan.scancode,
                            marka_type = mScan.format
                        )
                }
                "HAND_MADE" -> {
                    if (!checkBarcode(prefix = settings.getPrefix(), barcode = mScan.scancode)) return null
                    val scancodType = pickBarcodeType(barcode = mScan.scancode)
                    if (scancodType == "SCANCODE_TYPE_NOT_DEFINED") return null
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
