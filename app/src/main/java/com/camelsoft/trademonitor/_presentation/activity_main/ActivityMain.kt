package com.camelsoft.trademonitor._presentation.activity_main

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.camelsoft.trademonitor.BuildConfig
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.che
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showPermShouldGive
import com.camelsoft.trademonitor._presentation.utils.reqPermissions
import com.camelsoft.trademonitor._presentation.utils.writeDeveloper
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.ActivityMainBinding
import com.camelsoft.trademonitor.databinding.ActivityMainNavHeaderBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingNavHead: ActivityMainNavHeaderBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var rootFragmentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingNavHead = ActivityMainNavHeaderBinding.bind(binding.mainNavView.getHeaderView(0))
        setContentView(binding.root)

        weakContext = WeakReference<Context>(this)

        if (!che()) finish()

        // Показать версию в NavHeader`е
        bindingNavHead.navVer.text =  StringBuilder(resources.getString(R.string.version)+" "+BuildConfig.VERSION_NAME)

        // Меню
        //binding.activityMainContent.mainToolbar.setNavigationIcon(R.drawable.img_menu_24)
        setSupportActionBar(binding.activityMainContent.mainToolbar)
        //supportActionBar?.title = resources.getString(R.string.menu)
        val drawerLayout: DrawerLayout = binding.mainDrawerLayout
        val navigationView: NavigationView = binding.mainNavView
        navController = findNavController(R.id.mainNavHostFragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.fragGraphMain), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        rootFragmentId = navController.currentDestination?.id

        // Нажатия Navigation-списка
        binding.mainNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navMenuPrice -> { navController.navigate(R.id.fragGraphPrice) }
                R.id.navMenuAlko -> { navController.navigate(R.id.fragGraphAlko) }
                R.id.navMenuSettings -> { navController.navigate(R.id.fragGraphSettings) }
                R.id.navMenuAbout -> { writeDeveloper(weakContext.get()!!) }
                R.id.navMenuExit -> { finish() }
                else -> {}
            }
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        getPermissions()
    }

    // Нажатия кнопок верхнего меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp(appBarConfiguration)
//                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
//                true
            }
            else -> { super.onOptionsItemSelected(item) }
        }
    }

    // Запрос прав
    private fun getPermissions() {
        reqPermissions(weakContext.get()!!) { result ->
            when (result) {
                is EventsSync.Success -> {
                    result.data.let {
                        if (!result.data)
                            showPermShouldGive(weakContext.get()!!) { finish() }
                    }
                }
                is EventsSync.Error -> {
                    val backupMessage = resources.getString(R.string.error_in)+
                            " ActivityMain.getPermissions: "+
                            resources.getString(R.string.error_text_unknown)
                    showError(weakContext.get()!!, result.message?:backupMessage) {
                        finish()
                    }
                }
            }
        }
    }

    // Выход с задержкой по аппаратной кнопке
    private var back_pressed: Long = 0
    override fun onBackPressed() {
        if (rootFragmentId != null) {
            if (navController.currentDestination?.id == rootFragmentId) {
                if (back_pressed + 2000 > System.currentTimeMillis())
                    super.onBackPressed()
                else
                    Toast.makeText(baseContext, resources.getString(R.string.back_pressed), Toast.LENGTH_SHORT).show()
                back_pressed = System.currentTimeMillis()
            } else
                super.onBackPressed()
        } else
            showError(weakContext.get()!!, resources.getString(R.string.error_in)+
                    " ActivityMain.onBackPressed: "+
                    resources.getString(R.string.error_root_fragment_id)) { finish() }
    }
}