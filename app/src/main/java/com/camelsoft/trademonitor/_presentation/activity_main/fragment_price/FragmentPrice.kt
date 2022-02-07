package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.dialogs.showConfirm
import com.camelsoft.trademonitor._presentation.utils.dialogs.showError
import com.camelsoft.trademonitor.databinding.FragmentPriceBinding
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

        // Дабавить пустую сборку
        binding.btnAddColl.setOnClickListener {
            showConfirm(requireContext(),
                resources.getString(R.string.coll_add_title),
                resources.getString(R.string.coll_add_message)) {
                viewModel.onEventPrice(EventVmPrice.OnAddCollClick)
            }
        }

        // Обработка событий от View Model
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventUiPrice.collect { eventUiPrice ->
                when(eventUiPrice) {
                    is EventUiPrice.ShowError -> { showError(requireContext(), eventUiPrice.message) {} }
                    is EventUiPrice.ScrollToPos -> { binding.rvColl.scrollToPosition(eventUiPrice.position)}
                }
            }
        }

        // Список сборок
        binding.rvColl.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        viewModel.listPriceColl.observe(this, Observer { listPriceColl ->
            binding.rvColl.adapter = FragmentPriceAdapter(listPriceColl,
                // clickHolder
                // Переход к товарам внутри выбранной сборки
                { pos ->
                    val bundle = Bundle()
                    bundle.putParcelable("priceColl", listPriceColl[pos])
                    findNavController().navigate(R.id.action_fragGraphPrice_to_fragGraphPriceGoods, bundle)
                },
                // clickBtnShare
                // Поделиться выбранной сборкой
                { pos ->

                    Toast.makeText(requireContext(), "Поделиться выбранной сборкой. Позиция $pos", Toast.LENGTH_SHORT).show()

                },
                // clickBtnDelete
                // Удаление выбранной сборки
                { pos ->
                    showConfirm(requireContext(),
                        resources.getString(R.string.coll_del_title),
                        resources.getString(R.string.coll_del_message)+": ${listPriceColl[pos].note}") {
                        viewModel.onEventPrice(EventVmPrice.OnDeleteCollClick(pos))
                    }
                },
                // clickBtnUpdate
                // Обновления Примечания у выбранной сборки
                { mIntString ->
                    viewModel.onEventPrice(EventVmPrice.OnUpdateCollClick(mIntString.int, mIntString.string))
                })
        })
    }
}