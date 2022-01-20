package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showInfo
import com.camelsoft.trademonitor.databinding.FragmentPriceBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentPrice : Fragment() {
    private lateinit var binding: FragmentPriceBinding
    private val viewModel: FragmentPriceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddColl.setOnClickListener {
            viewModel.onEvent(EventVm.OnAddCollClick)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUi.collect { eventUi ->
                when(eventUi) {
                    is EventUi.ShowError -> { showError(requireContext(), eventUi.message) {} }
                    is EventUi.ShowInfo -> { showInfo(requireContext(), eventUi.message) {} }
                    is EventUi.ShowSnackbar -> {
                        Snackbar.make(
                            view,
                            eventUi.message,
                            Snackbar.LENGTH_INDEFINITE)
                            .setBackgroundTint(Color.MAGENTA)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setAction(eventUi.action) {
                            }


                            .show()

                    }
                }
            }
        }
    }
}