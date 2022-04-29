package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.camelsoft.trademonitor.databinding.FragmentAlkoMarkDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentAlkoMarkDetail : Fragment() {

    private lateinit var binding: FragmentAlkoMarkDetailBinding
    private lateinit var weakContext: WeakReference<Context>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlkoMarkDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
    }

}