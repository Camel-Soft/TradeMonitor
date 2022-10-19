package com.camelsoft.trademonitor._presentation.activity_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.getTelephonyItems
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.databinding.FragmentMainBinding

class FragmentMain : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainCardPrice.setOnClickListener {

            Toast.makeText(requireContext(),"${getTelephonyItems(getAppContext())}", Toast.LENGTH_LONG).show()

            //findNavController().navigate(R.id.action_fragGraphMain_to_fragGraphPrice)
        }

        binding.mainCardAlko.setOnClickListener {
            findNavController().navigate(R.id.action_fragGraphMain_to_fragGraphAlko)
        }
    }
}