package com.camelsoft.trademonitor._presentation.activity_settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.camelsoft.trademonitor.R

class FragmentSettings : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}