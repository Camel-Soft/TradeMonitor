package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camelsoft.trademonitor.common.Settings
import com.camelsoft.trademonitor.databinding.FragmentAlkoBinding
import java.lang.ref.WeakReference

class FragmentAlko : Fragment() {

    private lateinit var binding: FragmentAlkoBinding
    private lateinit var weakContext: WeakReference<Context>
    private val settings = Settings()

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
    }
}