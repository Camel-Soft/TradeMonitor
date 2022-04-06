package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import com.camelsoft.trademonitor._domain.models.MPriceGoods
import com.camelsoft.trademonitor._presentation.models.MScan

sealed class EventVmGoods {
    data class OnInsertGoods(val id_coll: Long, val scan: MScan): EventVmGoods()
    data class OnInsertGoodes(val id_coll: Long, val scanList: ArrayList<MScan>): EventVmGoods()
    data class OnDeleteGoods(val id_coll: Long, val pos: Int): EventVmGoods()
    data class OnUpdateGoods(val id_coll: Long, val pos: Int, val priceGoods: MPriceGoods): EventVmGoods()
    data class OnGetGoodes(val id_coll: Long): EventVmGoods()
}