package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

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
import com.camelsoft.trademonitor._presentation.utils.shareFile
import com.camelsoft.trademonitor.databinding.FragmentPriceBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentPrice : Fragment() {
    private lateinit var binding: FragmentPriceBinding
    private lateinit var weakContext: WeakReference<Context>
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

        weakContext = WeakReference<Context>(requireContext())

        // Дабавить пустую сборку
        binding.btnAddColl.setOnClickListener {
            showConfirm(weakContext.get()!!,
                resources.getString(R.string.coll_add_title),
                "${resources.getString(R.string.coll_add_message)}?") {
                viewModel.onEventPrice(EventVmPrice.OnAddCollClick)
            }
        }

        // Обработка событий от View Model
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUiPrice.collect { eventUiPrice ->
                when(eventUiPrice) {
                    is EventUiPrice.ShowErrorUi -> showError(weakContext.get()!!, eventUiPrice.message) {}
                    is EventUiPrice.ScrollToPos -> binding.rvColl.scrollToPosition(eventUiPrice.position)
                    is EventUiPrice.ShareFile -> shareFile(weakContext.get()!!, eventUiPrice.file, eventUiPrice.sign)
                }
            }
        }

        // Список сборок
        val adapterColl = FragmentPriceAdapter()
        // Выбрать сборку
        adapterColl.clickHolder = { pos ->
            val bundle = Bundle()
            bundle.putParcelable("priceColl", adapterColl.getList()[pos])
            findNavController().navigate(R.id.action_fragGraphPrice_to_fragGraphPriceGoods, bundle)
        }
        // Отправить (Share) сборку
        adapterColl.clickBtnShare = { pos ->
            viewModel.onEventPrice(EventVmPrice.OnShareCollClick(pos))
        }
        // Удалить сборку
        adapterColl.clickBtnDelete = { pos ->
            showConfirm(weakContext.get()!!,
                resources.getString(R.string.coll_del_title),
                resources.getString(R.string.coll_del_message)+": ${adapterColl.getList()[pos].note} ?") {
                viewModel.onEventPrice(EventVmPrice.OnDeleteCollClick(pos))
            }
        }
        // Обновить сборку (примечание)
        adapterColl.clickBtnUpdate = {
            viewModel.onEventPrice(EventVmPrice.OnUpdateCollClick(it.int, it.string))
        }
        binding.rvColl.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL,false)
        binding.rvColl.adapter = adapterColl
        viewModel.listPriceColl.observe(viewLifecycleOwner) { adapterColl.submitList(it) }
        viewModel.onEventPrice(EventVmPrice.OnGetColl)
    }
}