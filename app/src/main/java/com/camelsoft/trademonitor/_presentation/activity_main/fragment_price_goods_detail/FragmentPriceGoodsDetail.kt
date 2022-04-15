package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods_detail

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.databinding.FragmentPriceGoodsDetailBinding

class FragmentPriceGoodsDetail : Fragment() {
    private lateinit var binding: FragmentPriceGoodsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceGoodsDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Устанавливаем цвет верхней панели - белый
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setBackgroundDrawable(ColorDrawable(App.getAppContext().getColor(R.color.white)))

        // Устанавливаем заголовок
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
    }

    override fun onDestroyView() {
        // Устанавливаем цвет верхней панели - возвращаем назад
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar?.setBackgroundDrawable(ColorDrawable(App.getAppContext().getColor(R.color.yellow_200)))
        super.onDestroyView()
    }
}