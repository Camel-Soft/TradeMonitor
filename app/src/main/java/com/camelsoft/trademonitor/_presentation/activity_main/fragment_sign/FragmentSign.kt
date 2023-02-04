package com.camelsoft.trademonitor._presentation.activity_main.fragment_sign

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.utils.validateEmail
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.databinding.FragmentSignBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSign : Fragment() {
    private lateinit var binding: FragmentSignBinding
    private lateinit var weakContext: WeakReference<Context>
    @Inject lateinit var settings: Settings
    private val viewModel: FragmentSignViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weakContext = WeakReference<Context>(requireContext())
        handleEventsUi()
        handlePd()
        tabLayoutListener()
        loadAccount()
    }

    // Обработка событий пользовательского интерфейса
    private fun handleEventsUi() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsUi.collect { event ->
                when (event) {
                    is EventsUiSign.ShowError -> showError(weakContext.get()!!, event.message) {}
                    is EventsUiSign.ShowInfo -> showInfo(weakContext.get()!!, event.message) {}
                    is EventsUiSign.ShowToast -> Toast.makeText(weakContext.get()!!, event.message, Toast.LENGTH_LONG).show()
                    is EventsUiSign.Progress -> {
                        if (event.show) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.progressBar.isIndeterminate = true
                        }
                        else {
                            binding.progressBar.isIndeterminate = false
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }
                    is EventsUiSign.Close -> findNavController().popBackStack()
                }
            }
        }
    }

    // Галочка Согласие на обработку персональных данных
    private fun handlePd() {
        binding.checkHandlePd.setOnClickListener {
            if (binding.checkHandlePd.isChecked) binding.btnOk.isEnabled = true else binding.btnOk.isEnabled = false
        }
    }

    // Вешаем tab-листенер и жмем на 0-кнопку
    private fun tabLayoutListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    tabTap(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    tabTap(it.position)
                }
            }
        })

        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
    }

    // Обработка tab-нажатий
    private fun tabTap(position: Int) {
        when (position) {
            // Вход
            0 -> {
                binding.checkHandlePd.visibility = View.GONE
                binding.checkIsInforming.visibility = View.GONE
                binding.btnOk.isEnabled = true
                binding.btnOk.setOnClickListener(signIn)
            }
            // Регистрация
            1 -> {
                binding.checkHandlePd.visibility = View.VISIBLE
                binding.checkIsInforming.visibility = View.VISIBLE
                if (binding.checkHandlePd.isChecked) binding.btnOk.isEnabled = true else binding.btnOk.isEnabled = false
                binding.btnOk.setOnClickListener(signUp)
            }
            else -> showError(weakContext.get()!!, "[FragmentSign.tabTap] "+resources.getString(R.string.error_tab_handler)+": $position") {}
        }
    }

    // Листенер для кнопки Вход
    private val signIn: View.OnClickListener = View.OnClickListener {
        if (signStart()) viewModel.eventsVm(EventsVmSign.SignIn(EmlPass = Pair(
            first = binding.textEmail.text.toString().trim(),
            second = binding.textPass.text.toString().trim()
        )))
    }

    // Листенер для кнопки Регистрация
    private val signUp: View.OnClickListener = View.OnClickListener {
        if (signStart()) viewModel.eventsVm(EventsVmSign.SignUp(EmlPassInf = Triple(
            first = binding.textEmail.text.toString().trim(),
            second = binding.textPass.text.toString().trim(),
            third = binding.checkIsInforming.isChecked
        )))
    }

    private fun signStart(): Boolean {
        try {
            if (!binding.textEmail.text.toString().trim().validateEmail()) {
                showInfo(weakContext.get()!!, resources.getString(R.string.error_email)) {}
                return false
            }
            if (binding.textPass.text.toString().trim().length < 8) {
                showInfo(weakContext.get()!!, resources.getString(R.string.error_password)) {}
                return false
            }
            if (binding.checkSaveAccound.isChecked) {
                settings.putEmail(binding.textEmail.text.toString().trim())
                settings.putPassword(binding.textPass.text.toString().trim())
            }
            else {
                settings.putEmail(null)
                settings.putPassword(null)
            }
            settings.putLoginDate(System.currentTimeMillis())
            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            showError(weakContext.get()!!, "[FragmentSign.signStart] ${e.localizedMessage}") {}
            return false
        }
    }

    private fun loadAccount() {
        settings.getEmail()?.let { binding.textEmail.setText(it) }
        settings.getPassword()?.let { binding.textPass.setText(it) }
    }
}
