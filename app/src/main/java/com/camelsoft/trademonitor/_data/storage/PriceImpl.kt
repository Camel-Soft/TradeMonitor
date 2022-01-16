package com.camelsoft.trademonitor._data.storage

import com.camelsoft.trademonitor._data.storage.entities.REPriceColl
import com.camelsoft.trademonitor._data.storage.entities.REPriceGoods
import com.camelsoft.trademonitor._domain.entities.EPriceColl
import com.camelsoft.trademonitor._domain.entities.EPriceGoods

class PriceImpl(private val daoPrice: IDaoPrice): IPrice {

    override suspend fun insertPriceColl(priceColl: EPriceColl) {
        daoPrice.insertPriceColl(REPriceColl.toREPriceColl(priceColl))
    }

    override suspend fun insertPriceGoods(priceGoods: EPriceGoods) {
        daoPrice.insertPriceGoods(REPriceGoods.toREPriceGoods(priceGoods))
    }

    override suspend fun deletePriceColl(priceColl: EPriceColl) {
        daoPrice.deletePriceColl(REPriceColl.toREPriceColl(priceColl))
    }

    override suspend fun deletePriceGoods(priceGoods: EPriceGoods) {
        daoPrice.deletePriceGoods(REPriceGoods.toREPriceGoods(priceGoods))
    }

    override suspend fun updatePriceColl(priceColl: EPriceColl) {
        daoPrice.updatePriceColl(REPriceColl.toREPriceColl(priceColl))
    }

    override suspend fun updatePriceGoods(priceGoods: EPriceGoods) {
        daoPrice.updatePriceGoods(REPriceGoods.toREPriceGoods(priceGoods))
    }

    override suspend fun getPriceGoodes(id_coll: Long): List<EPriceGoods> {
        return daoPrice.getPriceGoodes(id_coll).map { it.toEPriceGoods() }
    }
}