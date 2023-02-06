package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.camelsoft.trademonitor.databinding.FragmentObjectBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class FragmentObject : Fragment() {
    private lateinit var binding: FragmentObjectBinding
    private lateinit var weakContext: WeakReference<Context>
    private val viewModel: FragmentObjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObjectBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weakContext = WeakReference<Context>(requireContext())
    }




}
