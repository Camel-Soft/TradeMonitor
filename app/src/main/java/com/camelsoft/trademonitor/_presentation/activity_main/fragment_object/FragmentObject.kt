package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showConfirm
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor._presentation.dialogs.showInfo
import com.camelsoft.trademonitor.databinding.FragmentObjectBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentObject : Fragment() {
    private lateinit var binding: FragmentObjectBinding
    private lateinit var weakContext: WeakReference<Context>
    private val viewModel: FragmentObjectViewModel by viewModels()
    private val fragmentObjectAdapter = FragmentObjectAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObjectBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weakContext = WeakReference<Context>(requireContext())

        rvSetter()
        eventsUiCollector()
        refreshAddressList()
    }

    // Установки RecyclerView
    private fun rvSetter() {
        fragmentObjectAdapter.setOnItemClickListener = { pos->
            val mAddress = fragmentObjectAdapter.getList()[pos]
            showConfirm(
                context = weakContext.get()!!,
                title = resources.getString(R.string.object_select),
                message = resources.getString(R.string.apply_current)+":\n\n${mAddress.name}\n${mAddress.address}   ?") {
                viewModel.eventsVm(EventsVmObject.ApplyAddress(mAddress = mAddress))
                findNavController().popBackStack()
            }
        }
        binding.rvObject.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL,false)
        binding.rvObject.adapter = fragmentObjectAdapter
        viewModel.listMAddress.observe(viewLifecycleOwner) {
            fragmentObjectAdapter.submitList(it)
        }
    }

    // Обработка событий Пользовательского Интерфейса
    private fun eventsUiCollector() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUiObject.collect { eventUi ->
                when(eventUi) {
                    is EventsUiObject.ShowError -> showError(weakContext.get()!!, eventUi.message) {}
                    is EventsUiObject.ShowInfo -> showInfo(weakContext.get()!!, eventUi.message) {}
                    is EventsUiObject.ShowToast -> Toast.makeText(weakContext.get()!!, eventUi.message, Toast.LENGTH_SHORT).show()
                    is EventsUiObject.Progress -> binding.refreshLayout.isRefreshing = eventUi.show
                    is EventsUiObject.PublicCurrent -> {
                        binding.textAddrBig.text = eventUi.mAddress.name
                        binding.textAddrSmall.text = eventUi.mAddress.address
                    }
                }
            }
        }
    }

    // Address List Refresh Listener
    private fun refreshAddressList() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.eventsVm(EventsVmObject.RefreshAddressList)
        }
    }
}
