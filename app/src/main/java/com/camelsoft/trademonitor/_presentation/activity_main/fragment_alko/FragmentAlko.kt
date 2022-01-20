package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camelsoft.trademonitor.R

class FragmentAlko : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alko, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentAlko()
    }
}