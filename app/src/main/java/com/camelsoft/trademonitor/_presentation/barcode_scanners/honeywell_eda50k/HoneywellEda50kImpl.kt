package com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.api.scan.IScanner
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor.common.events.EventsSync
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeFailureEvent
import com.honeywell.aidc.BarcodeReadEvent
import com.honeywell.aidc.BarcodeReader

class HoneywellEda50kImpl : IScanner {
    private lateinit var context: Context
    private lateinit var iResultScan: IResultScan
    private var scanProperties: Map<String, Any>? = null
    private lateinit var aidcManager: AidcManager
    private lateinit var barcodeReader: BarcodeReader
    private val handler = Handler(Looper.getMainLooper())

    override fun reg(context: Context, iResultScan: IResultScan, scanPropId: Int) {
        try {
            this.context = context
            this.iResultScan = iResultScan
            when (scanPropId) {
                1 -> scanProperties = honeyScanProp1D()
                2 -> scanProperties = honeyScanProp2D()
                3 -> scanProperties = honeyScanPropChecker()
                else -> scanProperties = null
            }
            scanProperties?.let { mapa->
                AidcManager.create(context) {
                    aidcManager = it
                    barcodeReader = aidcManager.createBarcodeReader()
                    barcodeReader.setProperties(addScanProperties(mapa))
                    // on/off scanner
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE, BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL)
                    barcodeReader.addBarcodeListener(barcodeListener)
                    barcodeReader.claim()
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            iResultScan.actionScan(EventsSync.Error("[HoneywellEda50kImpl.reg] ${e.localizedMessage}"))
        }
    }

    override fun unreg() {
        try {
            if (::barcodeReader.isInitialized) {
                barcodeReader.release()
                barcodeReader.removeBarcodeListener(barcodeListener)
                barcodeReader.close()
            }
            if (::aidcManager.isInitialized) aidcManager.close()
        }catch (e: Exception) {
            e.printStackTrace()
            iResultScan.actionScan(EventsSync.Error("[HoneywellEda50kImpl.unreg] ${e.localizedMessage}"))
        }
    }

    private val barcodeListener = object : BarcodeReader.BarcodeListener{
        override fun onBarcodeEvent(result: BarcodeReadEvent?) {
            handler.post(Runnable {
                try {
                    if (result != null)
                        iResultScan.actionScan(EventsSync.Success(MScan(result.barcodeData, codeIdConverter(result.codeId))))
                }catch (e: Exception) {
                    e.printStackTrace()
                    iResultScan.actionScan(EventsSync.Error("[HoneywellEda50kImpl.barcodeListener] ${e.localizedMessage}"))
                }
            })
        }

        override fun onFailureEvent(barcodeFailureEvent: BarcodeFailureEvent?) {
        }
    }

    private fun codeIdConverter(codeId: String): String {
        try {
            return when (codeId) {
                "d" -> "EAN_13"
                "D" -> "EAN_8"
                "c" -> "UPC_A"
                "E" -> "UPC_E"
                "s" -> "QR_CODE"
                "w" -> "DATA_MATRIX"
                "r" -> "PDF_417"
                else -> "unknown"
            }

        }catch (e: Exception) {
            e.printStackTrace()
            iResultScan.actionScan(EventsSync.Error("[HoneywellEda50kImpl.codeIdConverter] ${e.localizedMessage}"))
            return "error"
        }
    }

    private fun addScanProperties(propStart: Map<String, Any>): Map<String, Any> {
        try {
            val propFinish = HashMap<String, Any>()

            propFinish[BarcodeReader.PROPERTY_EAN_13_CHECK_DIGIT_TRANSMIT_ENABLED] = true
            propFinish[BarcodeReader.PROPERTY_EAN_13_ADDENDA_REQUIRED_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_EAN_13_ADDENDA_SEPARATOR_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_EAN_13_TWO_CHAR_ADDENDA_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_EAN_13_FIVE_CHAR_ADDENDA_ENABLED] = false

            propFinish[BarcodeReader.PROPERTY_EAN_8_CHECK_DIGIT_TRANSMIT_ENABLED] = true
            propFinish[BarcodeReader.PROPERTY_EAN_8_ADDENDA_REQUIRED_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_EAN_8_ADDENDA_SEPARATOR_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_EAN_8_TWO_CHAR_ADDENDA_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_EAN_8_FIVE_CHAR_ADDENDA_ENABLED] = false

            propFinish[BarcodeReader.PROPERTY_UPC_A_NUMBER_SYSTEM_TRANSMIT_ENABLED] = true
            propFinish[BarcodeReader.PROPERTY_UPC_A_CHECK_DIGIT_TRANSMIT_ENABLED] = true
            propFinish[BarcodeReader.PROPERTY_UPC_A_TRANSLATE_EAN13] = false
            propFinish[BarcodeReader.PROPERTY_UPC_A_ADDENDA_REQUIRED_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_A_ADDENDA_SEPARATOR_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_A_TWO_CHAR_ADDENDA_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_A_FIVE_CHAR_ADDENDA_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_A_COUPON_CODE_MODE_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_A_COMBINE_COUPON_CODE_MODE_ENABLED] = false

            propFinish[BarcodeReader.PROPERTY_UPC_E_NUMBER_SYSTEM_TRANSMIT_ENABLED] = true
            propFinish[BarcodeReader.PROPERTY_UPC_E_CHECK_DIGIT_TRANSMIT_ENABLED] = true
            propFinish[BarcodeReader.PROPERTY_UPC_E_EXPAND_TO_UPC_A] = false
            propFinish[BarcodeReader.PROPERTY_UPC_E_ADDENDA_REQUIRED_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_E_ADDENDA_SEPARATOR_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_E_TWO_CHAR_ADDENDA_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_E_FIVE_CHAR_ADDENDA_ENABLED] = false
            propFinish[BarcodeReader.PROPERTY_UPC_E_E1_ENABLED] = false

            propFinish[BarcodeReader.PROPERTY_DATAMATRIX_MINIMUM_LENGTH] = 1
            propFinish[BarcodeReader.PROPERTY_DATAMATRIX_MAXIMUM_LENGTH] = 3116

            propFinish[BarcodeReader.PROPERTY_QR_CODE_MINIMUM_LENGTH] = 1
            propFinish[BarcodeReader.PROPERTY_QR_CODE_MAXIMUM_LENGTH] = 7089

            propFinish[BarcodeReader.PROPERTY_PDF_417_MINIMUM_LENGTH] = 1
            propFinish[BarcodeReader.PROPERTY_PDF_417_MAXIMUM_LENGTH] = 2750

            // All
            propFinish[BarcodeReader.PROPERTY_DATA_PROCESSOR_LAUNCH_BROWSER] = false

            propStart.forEach { (string, any) ->
                propFinish[string] = any
            }

            return propFinish

        }catch (e: Exception) {
            e.printStackTrace()
            iResultScan.actionScan(EventsSync.Error("[HoneywellEda50kImpl.addScanProperties] ${e.localizedMessage}"))
            return emptyMap()
        }
    }
}

//    private lateinit var honeywellEDA50K: HoneywellEDA50K

//    in onCreate
//    honeywellEDA50K = HoneywellEDA50K(this, honeyListener, getScanProperties())

//    override fun onResume() {
//        super.onResume()
//        honeywellEDA50K.reg()
//    }

//    override fun onPause() {
//        super.onPause()
//        honeywellEDA50K.unreg()
//    }

//    private val honeyListener: IResultScan = object : IResultScan {
//        override fun actionScan(scan: EventsSync<MScan>) {
//            when (scan) {
//                is EventsSync.Success -> {
//                    Toast.makeText(this@ActivityMain, "Scancode: ${scan.data?.scancode}\nFormat: ${scan.data?.format}", Toast.LENGTH_LONG).show()
//                }
//                is EventsSync.Error -> {
//                    Toast.makeText(this@ActivityMain, "Error: ${scan.message}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

//    private fun getScanProperties(): Map<MGoodsBig, Any> {
//        val scanProperties = HashMap<MGoodsBig, Any>()
//        scanProperties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = true
//        scanProperties[BarcodeReader.PROPERTY_EAN_8_ENABLED] = true
//        scanProperties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
//        scanProperties[BarcodeReader.PROPERTY_UPC_E_ENABLED] = true
//        scanProperties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = true
//        scanProperties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
//        scanProperties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = true
//        return scanProperties
//    }
