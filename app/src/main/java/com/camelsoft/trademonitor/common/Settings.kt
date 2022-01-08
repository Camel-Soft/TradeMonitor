package com.camelsoft.trademonitor.common

import androidx.preference.PreferenceManager

class Settings {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(App.getAppContext())

    fun getScanner(): String {
        var scanner = "empty"
        prefManager.getString("scanner", "empty")?.let { scanner = it }
        return scanner
    }
}