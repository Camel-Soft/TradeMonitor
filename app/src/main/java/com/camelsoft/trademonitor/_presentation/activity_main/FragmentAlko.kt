package com.camelsoft.trademonitor._presentation.activity_main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.camelsoft.trademonitor.R

class FragmentAlko : Fragment() {

    private lateinit var viewModel: FragmentAlkoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alko, container, false)
    }

    companion object {
        fun newInstance() = FragmentAlko()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentAlkoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}