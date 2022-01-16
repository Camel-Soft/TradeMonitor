package com.camelsoft.trademonitor._data.storage

import com.camelsoft.trademonitor._domain.entities.EPriceColl
import com.camelsoft.trademonitor._domain.entities.EPriceGoods

interface IPrice {

    // Insert
    suspend fun insertPriceColl(priceColl: EPriceColl)
    suspend fun insertPriceGoods(priceGoods: EPriceGoods)

    // Delete
    suspend fun deletePriceColl(priceColl: EPriceColl)
    suspend fun deletePriceGoods(priceGoods: EPriceGoods)

    // Update
    suspend fun updatePriceColl(priceColl: EPriceColl)
    suspend fun updatePriceGoods(priceGoods: EPriceGoods)

    // Query
    suspend fun getPriceGoodes(id_coll: Long): List<EPriceGoods>
}