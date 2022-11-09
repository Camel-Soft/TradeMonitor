package com.camelsoft.trademonitor._presentation.activity_main.fragment_sign

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.activity_main.ActivityMainViewModel
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor.databinding.FragmentSignBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentSign : Fragment() {
    private lateinit var binding: FragmentSignBinding
    private lateinit var weakContext: WeakReference<Context>
    private val viewModel: ActivityMainViewModel by viewModels()

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
        tabLayoutListener()
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
                binding.checkIsInforming.visibility = View.INVISIBLE
                binding.btnOk.setOnClickListener(signIn)
            }
            // Регистрация
            1 -> {
                binding.checkIsInforming.visibility = View.VISIBLE
                binding.btnOk.setOnClickListener(signUp)
            }
            else -> showError(weakContext.get()!!, "[FragmentSign.tabTap] "+resources.getString(R.string.error_tab_handler)+": $position") {}
        }
    }

    // Листенер для кнопки Вход
    private val signIn: View.OnClickListener = View.OnClickListener {

    }

    // Листенер для кнопки Регистрация
    private val signUp: View.OnClickListener = View.OnClickListener {


    }
}
