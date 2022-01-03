package com.camelsoft.trademonitor._presentation.activitymain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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

        // Верхняя панель
        setSupportActionBar(binding.activityMainContent.mainToolbar)
        //supportActionBar?.title = resources.getString(R.string.title_menu)

        // Нажатия Navigation-списка
        binding.mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navMenuDir -> { }
                R.id.navMenuSettings -> {  }
                R.id.navMenuExit -> { finish() }
                else -> {}
            }
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // Нажатия кнопок верхнего меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    // Выход
    private var back_pressed: Long = 0
    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed()
        else
            Toast.makeText(baseContext, resources.getString(R.string.back_pressed), Toast.LENGTH_SHORT).show()
        back_pressed = System.currentTimeMillis()
    }
}