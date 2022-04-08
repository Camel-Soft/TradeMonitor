package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceGoods
import com.camelsoft.trademonitor._presentation.models.MScan

sealed class EventVmGoods {
    data class OnInsertGoods(val parentColl: MPriceColl, val scan: MScan): EventVmGoods()
    data class OnInsertGoodes(val parentColl: MPriceColl, val scanList: ArrayList<MScan>): EventVmGoods()
    data class OnDeleteGoods(val parentColl: MPriceColl, val pos: Int): EventVmGoods()
    data class OnUpdateGoods(val parentColl: MPriceColl, val pos: Int, val priceGoods: MPriceGoods): EventVmGoods()
    data class OnGetGoodes(val parentColl: MPriceColl): EventVmGoods()
}