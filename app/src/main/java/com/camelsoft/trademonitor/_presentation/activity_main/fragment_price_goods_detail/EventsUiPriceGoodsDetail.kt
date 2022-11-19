package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods_detail

import com.camelsoft.trademonitor._presentation.models.MGoodsBig

sealed class EventsUiPriceGoodsDetail {
    data class Success(val mGoodsBig: MGoodsBig): EventsUiPriceGoodsDetail()
    data class UnSuccess(val message: String): EventsUiPriceGoodsDetail()
    data class Update(val message: String): EventsUiPriceGoodsDetail()
    data class Progress(val show: Boolean): EventsUiPriceGoodsDetail()
    data class ShowInfo(val message: String): EventsUiPriceGoodsDetail()
    data class ShowError(val message: String): EventsUiPriceGoodsDetail()
}
