package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camelsoft.trademonitor.databinding.FragmentAlkoMarkBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentAlkoMark : Fragment() {

    private lateinit var binding: FragmentAlkoMarkBinding
    private lateinit var weakContext: WeakReference<Context>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlkoMarkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weakContext = WeakReference<Context>(requireContext())
    }
}