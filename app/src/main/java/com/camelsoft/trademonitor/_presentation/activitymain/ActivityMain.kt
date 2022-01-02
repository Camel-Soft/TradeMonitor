package com.camelsoft.trademonitor._presentation.activitymain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navMenuMonitor -> { Toast.makeText(this@ActivityMain, "Hello", Toast.LENGTH_SHORT).show() }
                    R.id.navMenuSettings -> { }
                    R.id.navMenuExit -> { finish() }
                    else -> {}
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

        }

    }
}