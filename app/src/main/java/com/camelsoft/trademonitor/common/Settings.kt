package com.camelsoft.trademonitor.common

import android.content.Context
import androidx.preference.PreferenceManager
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

class Settings {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(getAppContext())

    fun getPrefix(): String {
        var prefix = "99"
        prefManager.getString("weight_prefix", "99")?.let { prefix = it }
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
}
