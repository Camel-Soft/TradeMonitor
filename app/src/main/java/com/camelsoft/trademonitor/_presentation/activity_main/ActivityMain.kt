package com.camelsoft.trademonitor._presentation.activity_main

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.camelsoft.trademonitor.BuildConfig
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.dialogs.showPermShouldGive
import com.camelsoft.trademonitor._presentation.models.user.MUser
import com.camelsoft.trademonitor._presentation.utils.reqPermissions
import com.camelsoft.trademonitor._presentation.utils.timeToStringShort
import com.camelsoft.trademonitor._presentation.utils.writeDeveloper
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.common.events.EventsSync
import com.camelsoft.trademonitor.databinding.ActivityMainBinding
import com.camelsoft.trademonitor.databinding.ActivityMainNavHeaderBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingNavHead: ActivityMainNavHeaderBinding
    private lateinit var weakContext: WeakReference<Context>
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var rootFragmentId: Int? = null
    private val viewModel: ActivityMainViewModel by viewModels()
    @Inject lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingNavHead = ActivityMainNavHeaderBinding.bind(binding.mainNavView.getHeaderView(0))
        setContentView(binding.root)

        weakContext = WeakReference<Context>(this)

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
                R.id.navMenuPrice -> viewModel.eventsVm(EventsVmMainActivity.VerifyTaskPrice)
                R.id.navMenuAlko -> viewModel.eventsVm(EventsVmMainActivity.VerifyTaskAlko)
                R.id.navMenuChecker -> viewModel.eventsVm(EventsVmMainActivity.VerifyTaskChecker)
                R.id.navMenuSettings -> navController.navigate(R.id.fragGraphSettings)
                R.id.navMenuAbout -> writeDeveloper(weakContext.get()!!)
                R.id.navMenuExit -> finish()
                else -> {}
            }
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navControllerListener()
        btnSignListener()
        btnLogoutListener()
        eventsUi()
        getPermissions()
        showWorkModeOffline()
    }

    // Рулит отображением пункта меню OFF-Line источник
    private fun showWorkModeOffline() {
        settings.workModeOfflineLiveData.observe(this) {
            binding.mainNavView.menu.getItem(0).subMenu?.getItem(3)?.isVisible = it
        }
    }

    // Обработка событий от View Model
    private fun eventsUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventsUi.collect { eventUiMain ->
                when(eventUiMain) {
                    is EventsUiMainActivity.ShowError -> showError(weakContext.get()!!, eventUiMain.message) {}
                    is EventsUiMainActivity.ShowInfo -> showInfo(weakContext.get()!!, eventUiMain.message) {}
                    is EventsUiMainActivity.ShowToast -> Toast.makeText(weakContext.get()!!, eventUiMain.message, Toast.LENGTH_SHORT).show()
                    is EventsUiMainActivity.LogIn -> logInView(eventUiMain.mUser)
                    is EventsUiMainActivity.LogOut -> logOutView()
                    is EventsUiMainActivity.HandleTaskPrice -> {
                        if (eventUiMain.run) navController.navigate(R.id.fragGraphPrice)
                        else showInfo(weakContext.get()!!, resources.getString(R.string.info_need_registration)) {
                            navController.navigate(R.id.fragGraphSign)
                        }
                    }
                    is EventsUiMainActivity.HandleTaskAlko -> {
                        if (eventUiMain.run) navController.navigate(R.id.fragGraphAlko)
                        else showInfo(weakContext.get()!!, resources.getString(R.string.info_need_registration)) {
                            navController.navigate(R.id.fragGraphSign)
                        }
                    }
                    is EventsUiMainActivity.HandleTaskChecker -> {
                        if (eventUiMain.run) navController.navigate(R.id.fragGraphChecker)
                        else showInfo(weakContext.get()!!, resources.getString(R.string.info_need_registration)) {
                            navController.navigate(R.id.fragGraphSign)
                        }
                    }
                }
            }
        }
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
                            resources.getString(R.string.error_unknown)
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

    // Кнопка Вход/Регистрация
    private fun btnSignListener() {
        bindingNavHead.cardSign.setOnClickListener {
            navController.navigate(R.id.fragGraphSign)
            if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START))
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    // Кнопка Выход(Logout)
    private fun btnLogoutListener() {
        bindingNavHead.btnLogout.setOnClickListener {
            viewModel.eventsVm(EventsVmMainActivity.Logout)
            if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START))
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun navControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Шторка (левая) открывается только с кнопки, или нет
            if (destination.displayName == "com.camelsoft.trademonitor:id/fragGraphMain")
                binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            else
                binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    // Отрисовка выхода
    private fun logOutView() {
        bindingNavHead.signText1.text = ""
        bindingNavHead.signText2.text = ""
        bindingNavHead.signText3.text = ""
        bindingNavHead.signText4.text = ""
        bindingNavHead.signText1.visibility = View.INVISIBLE
        bindingNavHead.signText2.visibility = View.INVISIBLE
        bindingNavHead.signText3.visibility = View.INVISIBLE
        bindingNavHead.signText4.visibility = View.INVISIBLE
        bindingNavHead.btnLogout.visibility = View.INVISIBLE
        bindingNavHead.cardSign.visibility = View.VISIBLE
    }

    // Отрисовка входа
    private fun logInView(mUser: MUser) {
        var mob = ""
        var srv = ""
        if (mUser.isActiveDev) {
            when (mUser.licLevelDev) {
                -1 -> mob = "${resources.getString(R.string.dev_mob)} - ${resources.getString(R.string.lic_trial)}"
                0 -> mob = "${resources.getString(R.string.dev_mob)} - ${resources.getString(R.string.lic_zero)}"
                1 -> mob = "${resources.getString(R.string.dev_mob)} - ${resources.getString(R.string.lic_base)}"
                else -> mob = "${resources.getString(R.string.dev_mob)} - ${resources.getString(R.string.lic_empty)}"
            }
        }
        if (mUser.isActiveSrv) {
            when (mUser.licLevelSrv) {
                -1 -> srv = "${resources.getString(R.string.dev_srv)} - ${resources.getString(R.string.lic_trial)}"
                0 -> srv = "${resources.getString(R.string.dev_srv)} - ${resources.getString(R.string.lic_zero)}"
                1 -> srv = "${resources.getString(R.string.dev_srv)} - ${resources.getString(R.string.lic_base)}"
                else -> srv = "${resources.getString(R.string.dev_srv)} - ${resources.getString(R.string.lic_empty)}"
            }
        }
        bindingNavHead.cardSign.visibility = View.INVISIBLE
        bindingNavHead.signText1.text = mUser.email
        bindingNavHead.signText2.text = "${resources.getString(R.string.lic_exp)} ${timeToStringShort(mUser.expTime)}"
        bindingNavHead.signText3.text = mob
        bindingNavHead.signText4.text = srv
        bindingNavHead.signText1.visibility = View.VISIBLE
        bindingNavHead.signText2.visibility = View.VISIBLE
        bindingNavHead.signText3.visibility = View.VISIBLE
        bindingNavHead.signText4.visibility = View.VISIBLE
        bindingNavHead.btnLogout.visibility = View.VISIBLE
    }
}
