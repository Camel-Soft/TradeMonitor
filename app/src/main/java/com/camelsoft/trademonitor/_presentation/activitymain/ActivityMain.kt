package com.camelsoft.trademonitor._presentation.activitymain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.activityMainContent.mainToolbar)

        binding.apply {
            mainNavView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navMenuHome -> { startFragment(FragmentMainHome.newInstance()) }
                    R.id.navMenuDir -> { }
                    R.id.navMenuSettings -> { startFragment(FragmentMainSettings.newInstance()) }
                    R.id.navMenuExit -> { finish() }
                    else -> {}
                }
                mainDrawerLayout.closeDrawer(GravityCompat.START)
                true
            }

        }

    }

    override fun onResume() {
        super.onResume()
        startFragment(FragmentMainHome.newInstance())
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrameLayout, fragment)
            .commit()
    }

    private var back_pressed: Long = 0
    override fun onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed()
        else
            Toast.makeText(baseContext, resources.getString(R.string.back_pressed), Toast.LENGTH_SHORT).show()
        back_pressed = System.currentTimeMillis()
    }
}