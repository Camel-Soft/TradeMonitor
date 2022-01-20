package com.camelsoft.trademonitor._data.storage

import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceGoods

interface IPrice {

    // Insert
    suspend fun insertPriceColl(priceColl: MPriceColl)
    suspend fun insertPriceGoods(priceGoods: MPriceGoods)

    // Delete
    suspend fun deletePriceColl(priceColl: MPriceColl)
    suspend fun deletePriceGoods(priceGoods: MPriceGoods)

    // Update
    suspend fun updatePriceColl(priceColl: MPriceColl)
    suspend fun updatePriceGoods(priceGoods: MPriceGoods)

    // Query
    suspend fun getPriceCollAll(): List<MPriceColl>
    suspend fun getPriceGoodes(id_coll: Long): List<MPriceGoods>
}