package com.camelsoft.trademonitor._presentation.activity_settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.databinding.ActivitySettingsBinding

class ActivitySettings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Верхняя панель
        binding.settingsToolbar.setNavigationIcon(R.drawable.img_arrow_back_black_24)
        setSupportActionBar(binding.settingsToolbar)
        supportActionBar?.title = resources.getString(R.string.settings)
    }

    // Кнопка "Назад" на ActionBar(toolbar)
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}