package com.camelsoft.trademonitor.common.settings

import android.content.Context
import androidx.preference.PreferenceManager
import com.camelsoft.trademonitor._presentation.models.MOffline
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

object Settings {

    private val settingsManager = PreferenceManager.getDefaultSharedPreferences(getAppContext())

    fun getConnSrvLoc(): String {
        var connSrvLoc = ""
        settingsManager.getString("conn_server_loc", "")?.let { connSrvLoc = it.trim() }
        return connSrvLoc
    }

    fun getPrefix(): String {
        var prefix = "99"
        settingsManager.getString("weight_prefix", "99")?.let { prefix = it.trim() }
        return prefix
    }

    fun getAutoCorrBarcode(): Boolean {
        var autoCorrBar = true
        settingsManager.getBoolean("barcode_autocorrection", true).let { autoCorrBar = it }
        return autoCorrBar
    }

    fun getExportFileFormat(): String {
        var format = "excel"
        settingsManager.getString("export_file_format", "excel")?.let { format = it }
        return format
    }

    fun getScanner(): String {
        var scanner = "empty"
        settingsManager.getString("scanner", "empty")?.let { scanner = it }
        return scanner
    }

    fun getWorkModeOffline(): Boolean {
        var workModeOffline = false
        settingsManager.getBoolean("work_mode_offline", false).let { workModeOffline = it }
        return workModeOffline
    }
    val workModeOfflineLiveData = settingsManager.booleanLiveData("work_mode_offline", false)

// *************************************************************************************************

    private val privateManager = getAppContext().getSharedPreferences("privateManager", Context.MODE_PRIVATE)

    fun putEmail(email: String?) {
        synchronized(this) {
            val editor = privateManager.edit()
            editor.putString("email", email)
            editor.apply()
        }
    }

    fun getEmail(): String? {
        if (!privateManager.contains("email")) return null
        else return privateManager.getString("email", null)
    }

    fun putPassword(password: String?) {
        synchronized(this) {
            val editor = privateManager.edit()
            editor.putString("password", password)
            editor.apply()
        }
    }

    fun getPassword(): String? {
        if (!privateManager.contains("password")) return null
        else return privateManager.getString("password", null)
    }

    fun putPrice(price: String?) {
        synchronized(this) {
            val editor = privateManager.edit()
            editor.putString("price", price)
            editor.apply()
        }
    }

    fun getPrice(): String? {
        if (!privateManager.contains("price")) return null
        else return privateManager.getString("price", null)
    }

    fun putMOffline(mOffline: MOffline) {
        synchronized(this) {
            val editor = privateManager.edit()
            editor.putBoolean("offl_isRunning", mOffline.isRunning)
            editor.putString("offl_info", mOffline.info)
            editor.putInt("offl_stageCurrent", mOffline.stageCurrent)
            editor.putInt("offl_stageTotal", mOffline.stageTotal)
            editor.putString("offl_stageName", mOffline.stageName)
            editor.putInt("offl_stagePercent", mOffline.stagePercent)
            editor.apply()
        }
    }

    fun getMOffline(): MOffline {
        return MOffline(
            isRunning = privateManager.getBoolean("offl_isRunning",false),
            info = privateManager.getString("offl_info", "") as String,
            stageCurrent = privateManager.getInt("offl_stageCurrent", -1),
            stageTotal = privateManager.getInt("offl_stageTotal", -1),
            stageName = privateManager.getString("offl_stageName", "") as String,
            stagePercent = privateManager.getInt("offl_stagePercent", 0)
        )
    }
    val mOfflineLiveData = privateManager.mOfflineLiveData()
}
