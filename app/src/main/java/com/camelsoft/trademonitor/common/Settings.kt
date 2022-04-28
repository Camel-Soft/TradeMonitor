package com.camelsoft.trademonitor.common

import androidx.preference.PreferenceManager

class Settings {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(App.getAppContext())

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
}