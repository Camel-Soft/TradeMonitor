package com.camelsoft.trademonitor.common

import androidx.preference.PreferenceManager

class Settings {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(App.getAppContext())

    fun getPrefix(): String {
        var prefix = "99"
        prefManager.getString("weight_prefix", "99")?.let { prefix = it }
        return prefix

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