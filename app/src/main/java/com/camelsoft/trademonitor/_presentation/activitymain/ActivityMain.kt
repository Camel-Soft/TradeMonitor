package com.camelsoft.trademonitor._presentation.activitymain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.Permissions
import com.camelsoft.trademonitor._presentation.utils.dialogs.ShowError
import com.camelsoft.trademonitor._presentation.utils.dialogs.ShowPermShouldGive
import com.camelsoft.trademonitor.common.resource.ResSync
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

    override fun onStart() {
        super.onStart()

        // Запрос прав
        Permissions(this) { result ->
            when (result) {
                is ResSync.Success -> {
                    result.data?.let {
                        if (!result.data)
                            ShowPermShouldGive(this) { finish() }
                    }
                }
                is ResSync.Error -> {
                    val backupMessage = resources.getString(R.string.error_in)+
                            " onResume.Permissions: "+
                            resources.getString(R.string.error_text_unknown)
                    ShowError(this, result.message?:backupMessage) {
                        finish()
                    }
                }
            }
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