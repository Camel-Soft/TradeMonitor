package com.camelsoft.trademonitor._presentation.activity_main.fragment_main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentMain : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var weakContext: WeakReference<Context>
    private val viewModel: FragmentMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
        eventsUi()
        btnsTasksListeners()
    }

    // Листенеры кнопок задач
    private fun btnsTasksListeners() {
        binding.mainCardPrice.setOnClickListener {
            viewModel.eventsVm(EventsVmMain.VerifyTaskPrice)
        }
        binding.mainCardAlko.setOnClickListener {
            viewModel.eventsVm(EventsVmMain.VerifyTaskAlko)
        }
    }

    // Обработка событий от View Model
    private fun eventsUi() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventsUi.collect { eventUiMain ->
                when (eventUiMain) {
                    is EventsUiMain.HandleTaskPrice -> {
                        if (eventUiMain.run) findNavController().navigate(R.id.action_fragGraphMain_to_fragGraphPrice)
                        else showInfo(weakContext.get()!!, resources.getString(R.string.info_need_registration)) {
                            findNavController().navigate(R.id.fragGraphSign)
                        }
                    }
                    is EventsUiMain.HandleTaskAlko -> {
                        if (eventUiMain.run) findNavController().navigate(R.id.action_fragGraphMain_to_fragGraphAlko)
                        else showInfo(weakContext.get()!!, resources.getString(R.string.info_need_registration)) {
                            findNavController().navigate(R.id.fragGraphSign)
                        }
                    }
                }
            }
        }
    }
}
