package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor.databinding.FragmentPriceGoodsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPriceGoods : Fragment() {
    private lateinit var binding: FragmentPriceGoodsBinding
    private val viewModel: FragmentPriceGoodsViewModel by viewModels()
    private var parentPriceColl: MPriceColl? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceGoodsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Забираем данные о заголовке сборки в переменную
        parentPriceColl = arguments?.getParcelable("priceColl")
        // Устанавливаем звголовок
        (requireActivity() as AppCompatActivity).supportActionBar?.title = parentPriceColl?.note



    }
}