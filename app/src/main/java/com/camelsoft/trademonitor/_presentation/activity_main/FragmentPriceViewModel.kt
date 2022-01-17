package com.camelsoft.trademonitor._presentation.activity_main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.camelsoft.trademonitor._data.storage.IPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentPriceViewModel @Inject constructor(
    private val iPrice: IPrice
) : ViewModel() {

    val ttt = "str"

    init {
        Log.d("vm", "FragmentPriceViewModel start")
    }

    override fun onCleared() {
        Log.d("vm", "FragmentPriceViewModel cleared")
        super.onCleared()
    }




    
}