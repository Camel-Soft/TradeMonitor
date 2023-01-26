package com.camelsoft.trademonitor._presentation.activity_main.fragment_settings

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App.Companion.showServerLicensing

class FragmentSettings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        // "Префикс весового товара" - форматируем поле
        val editWeight = preferenceManager.findPreference<EditTextPreference>("weight_prefix")
        editWeight?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
            it.filters = arrayOf<InputFilter>(LengthFilter(2))
            it.setSelection(it.text.length)
        }
        editWeight?.setOnPreferenceChangeListener { _, newValue ->
            newValue.toString().length == 2
        }

        // Сервер Лицензирования
        val editServerLicensing = preferenceManager.findPreference<EditTextPreference>("conn_server_licensing")
        editServerLicensing?.isVisible = showServerLicensing
    }
}
