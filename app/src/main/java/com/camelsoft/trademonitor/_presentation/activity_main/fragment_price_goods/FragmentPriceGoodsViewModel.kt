package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import androidx.lifecycle.ViewModel
import com.camelsoft.trademonitor._data.storage.IPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentPriceGoodsViewModel @Inject constructor(
    private val iPrice: IPrice
): ViewModel() {
}