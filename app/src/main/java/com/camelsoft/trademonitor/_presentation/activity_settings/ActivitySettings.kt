package com.camelsoft.trademonitor._presentation.activity_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.camelsoft.trademonitor.databinding.ActivitySettingsBinding

class ActivitySettings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}