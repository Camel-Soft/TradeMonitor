package com.camelsoft.trademonitor._presentation.activity_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camelsoft.trademonitor.R

class FragmentPrice : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_price, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentPrice()
    }
}