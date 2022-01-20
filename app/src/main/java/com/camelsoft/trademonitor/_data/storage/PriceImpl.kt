package com.camelsoft.trademonitor._data.storage

import com.camelsoft.trademonitor._data.storage.entities.REPriceColl
import com.camelsoft.trademonitor._data.storage.entities.REPriceGoods
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceGoods

class PriceImpl(private val daoPrice: IDaoPrice): IPrice {

    override suspend fun insertPriceColl(priceColl: MPriceColl) {
        daoPrice.insertPriceColl(REPriceColl.toREPriceColl(priceColl))
    }

    override suspend fun insertPriceGoods(priceGoods: MPriceGoods) {
        daoPrice.insertPriceGoods(REPriceGoods.toREPriceGoods(priceGoods))
    }

    override suspend fun deletePriceColl(priceColl: MPriceColl) {
        daoPrice.deletePriceColl(REPriceColl.toREPriceColl(priceColl))
    }

    override suspend fun deletePriceGoods(priceGoods: MPriceGoods) {
        daoPrice.deletePriceGoods(REPriceGoods.toREPriceGoods(priceGoods))
    }

    override suspend fun updatePriceColl(priceColl: MPriceColl) {
        daoPrice.updatePriceColl(REPriceColl.toREPriceColl(priceColl))
    }

    override suspend fun updatePriceGoods(priceGoods: MPriceGoods) {
        daoPrice.updatePriceGoods(REPriceGoods.toREPriceGoods(priceGoods))
    }

    override suspend fun getPriceCollAll(): List<MPriceColl> {
        return daoPrice.getPriceCollAll().map { it.toMPriceColl() }

    }

    override suspend fun getPriceGoodes(id_coll: Long): List<MPriceGoods> {
        return daoPrice.getPriceGoodes(id_coll).map { it.toMPriceGoods() }
    }
}