package com.camelsoft.trademonitor.common.settings

import android.content.Context
import androidx.preference.PreferenceManager
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

object Settings {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(getAppContext())

    fun getConnSrvLoc(): String {
        var connSrvLoc = ""
        prefManager.getString("conn_server_loc", "")?.let { connSrvLoc = it.trim() }
        return connSrvLoc
    }

    fun getPrefix(): String {
        var prefix = "99"
        prefManager.getString("weight_prefix", "99")?.let { prefix = it.trim() }
        return prefix
    }

    fun getAutoCorrBarcode(): Boolean {
        var autoCorrBar = true
        prefManager.getBoolean("barcode_autocorrection", true).let { autoCorrBar = it }
        return autoCorrBar
    }

    fun getExportFileFormat(): String {
        var format = "excel"
        prefManager.getString("export_file_format", "excel")?.let { format = it }
        return format
    }

    fun getScanner(): String {
        var scanner = "empty"
        prefManager.getString("scanner", "empty")?.let { scanner = it }
        return scanner
    }

    fun getWorkModeOffline(): Boolean {
        var workModeOffline = false
        prefManager.getBoolean("work_mode_offline", false).let { workModeOffline = it }
        return workModeOffline
    }
    val workModeOfflineLiveData = prefManager.booleanLiveData("work_mode_offline", false)

    // ****************************************************************************************

    private val privateManager = getAppContext().getSharedPreferences("pm", Context.MODE_PRIVATE)

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
}