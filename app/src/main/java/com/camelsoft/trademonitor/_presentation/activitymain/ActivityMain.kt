package com.camelsoft.trademonitor._presentation.activitymain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.camelsoft.trademonitor.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}