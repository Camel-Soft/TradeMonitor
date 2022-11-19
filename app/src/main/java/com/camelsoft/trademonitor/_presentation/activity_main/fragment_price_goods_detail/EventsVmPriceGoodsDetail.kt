package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods_detail

import com.camelsoft.trademonitor._presentation.models.MGoodsBig

sealed class EventsVmPriceGoodsDetail {
    data class SendRequestGoods(val mGoodsBig: MGoodsBig): EventsVmPriceGoodsDetail()
}
