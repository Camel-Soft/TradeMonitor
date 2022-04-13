package com.camelsoft.trademonitor.common

import androidx.preference.PreferenceManager

class Settings {

    private val prefManager = PreferenceManager.getDefaultSharedPreferences(App.getAppContext())

    fun getPrefix(): String {
        var prefix = "99"
        prefManager.getString("weight_prefix", "99")?.let { prefix = it }
        return prefix

    }

    fun getScanner(): String {
        var scanner = "empty"
        prefManager.getString("scanner", "empty")?.let { scanner = it }
        return scanner
    }
}