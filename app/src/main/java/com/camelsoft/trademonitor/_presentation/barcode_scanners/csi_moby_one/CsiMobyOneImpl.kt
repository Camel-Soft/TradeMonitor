package com.camelsoft.trademonitor._presentation.barcode_scanners.csi_moby_one

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.api.scan.IScanner
import com.camelsoft.trademonitor._presentation.models.MScan
import com.camelsoft.trademonitor._presentation.utils.beepOnce
import com.camelsoft.trademonitor._presentation.utils.scan.EanUpcCheckDigit
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor._presentation.utils.vibrateOnce
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsSync

class CsiMobyOneImpl : IScanner {
    private lateinit var context: Context
    private lateinit var iResultScan: IResultScan
    private var scanProperties: Map<String, Any>? = null
    private val SCAN_ACTION = "scan.rcv.message"
    private val intentFilter = IntentFilter(SCAN_ACTION)
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                if (intent == null) return
                if (intent.action != SCAN_ACTION) return
                val rowScancodeType = intent.getStringExtra("barcodeType") ?: return
                val rowScancodeData = intent.getStringExtra("barcodeData") ?: return
                val scancodeType = getScanType(rowScancodeType)
                val scancodeData = scanCorrect(scancodeType, rowScancodeData)

                scanProperties?.let { mapa->
                    mapa.forEach { mapEntry->
                        if (mapEntry.key == scancodeType) {
                            vibrateOnce()
                            beepOnce()
                            iResultScan.actionScan(EventsSync.Success(MScan(scancode = scancodeData, format = scancodeType)))
                            return@forEach
                        }
                    }
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                e.localizedMessage?.let {
                    iResultScan.actionScan(EventsSync.Error(message = "[CsiMobyOneImpl.broadcastReceiver] $it"))
                }
            }
        }
    }

    override fun reg(context: Context, iResultScan: IResultScan, scanPropId: Int) {
        try {
            this.context = context
            this.iResultScan = iResultScan
            val scanDevice = ScanDevice() // Валится здесь, если устройство не CSI
            scanDevice.resetScan()
            scanDevice.outScanMode = 0
            scanDevice.openScan()
            scanDevice.indicatorLightMode = 1
            scanDevice.setScanUnVibrate()
            scanDevice.setScanUnBeep()
            when (scanPropId) {
                1 -> scanProperties = csiScanProp1D()
                2 -> scanProperties = csiScanProp2D()
                3 -> scanProperties = csiScanPropChecker()
                else -> scanProperties = null
            }
            this.context.registerReceiver(broadcastReceiver, intentFilter)
        }
        catch (_: Exception) {}
    }

    override fun unreg() {
        try { context.unregisterReceiver(broadcastReceiver) } catch (_: Exception) {}
    }

    /*
    Принимаем, что настройки сканера сброшены на default. При таком раскладе коды выглядят так:
    EAN_8 - 4608927 - без контрольки
    UPC_E - 123073 - без лидирующего 0 и без контрольки
    Вот эти два кода и корректируем
     */
    private fun scanCorrect(scancodeType: String, rowScancodeData: String): String {
        when (scancodeType) {
            getAppContext().resources.getString(R.string.ean_8) -> {
                if (rowScancodeData.length == 7)
                    return rowScancodeData+EanUpcCheckDigit(rowScancodeData).checkDigit
                else
                    return rowScancodeData
            }
            getAppContext().resources.getString(R.string.upc_e) -> {
                if (rowScancodeData.length == 6)
                    return "0"+rowScancodeData+EanUpcCheckDigit("0$rowScancodeData").checkDigit
                else
                    return rowScancodeData
            }
            else -> return rowScancodeData
        }
    }
}
