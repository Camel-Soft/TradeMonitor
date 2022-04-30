package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.dialogs.showConfirm
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor._presentation.utils.dialogs.showInfo
import com.camelsoft.trademonitor._presentation.utils.shareFile
import com.camelsoft.trademonitor.databinding.FragmentAlkoBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentAlko : Fragment() {
    private lateinit var binding: FragmentAlkoBinding
    private lateinit var weakContext: WeakReference<Context>
    private val viewModel: FragmentAlkoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlkoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())

        // Дабавить пустую сборку
        binding.btnAddColl.setOnClickListener {
            showConfirm(weakContext.get()!!,
                resources.getString(R.string.coll_add_title),
                "${resources.getString(R.string.coll_add_message)}?") {
                viewModel.onEventAlkoColl(EventVmAlkoColl.OnAddCollClick)
            }
        }

        // Обработка событий от View Model
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUiAlkoColl.collect { eventUiAlkoColl ->
                when(eventUiAlkoColl) {
                    is EventUiAlkoColl.ShowErrorUi -> showError(weakContext.get()!!, eventUiAlkoColl.message) {}
                    is EventUiAlkoColl.ShowInfoUi -> showInfo(weakContext.get()!!, eventUiAlkoColl.message) {}
                    is EventUiAlkoColl.ScrollToPos -> binding.rvColl.scrollToPosition(eventUiAlkoColl.position)
                    is EventUiAlkoColl.ShareFile -> shareFile(weakContext.get()!!, eventUiAlkoColl.file, eventUiAlkoColl.sign)
                }
            }
        }

        // Список сборок
        val adapterAlkoColl = FragmentAlkoCollAdapter()
        // Выбрать сборку
        adapterAlkoColl.clickHolder = { pos ->
            val bundle = Bundle()
            bundle.putParcelable("alkoColl", adapterAlkoColl.getList()[pos])
            findNavController().navigate(R.id.action_fragGraphAlko_to_fragGraphAlkoMark, bundle)
        }
        // Отправить (Share) сборку
        adapterAlkoColl.clickBtnShare = { pos ->
            viewModel.onEventAlkoColl(EventVmAlkoColl.OnShareCollClick(pos))
        }
        // Удалить сборку
        adapterAlkoColl.clickBtnDelete = { pos ->
            showConfirm(weakContext.get()!!,
                resources.getString(R.string.coll_del_title),
                resources.getString(R.string.coll_del_message)+": ${adapterAlkoColl.getList()[pos].note} ?") {
                viewModel.onEventAlkoColl(EventVmAlkoColl.OnDeleteCollClick(pos))
            }
        }
        // Обновить сборку (примечание)
        adapterAlkoColl.clickBtnUpdate = {
            viewModel.onEventAlkoColl(EventVmAlkoColl.OnUpdateCollClick(it.int, it.string))
        }
        binding.rvColl.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL,false)
        binding.rvColl.adapter = adapterAlkoColl
        viewModel.listAlkoColl.observe(viewLifecycleOwner) { adapterAlkoColl.submitList(it) }
        viewModel.onEventAlkoColl(EventVmAlkoColl.OnGetColl)
    }
}