package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods_detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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


        activity?.actionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // Устанавливаем заголовок
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Заголовок"
    }
}