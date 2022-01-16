package com.camelsoft.trademonitor._data.storage

import androidx.room.*
import com.camelsoft.trademonitor._data.storage.entities.REPriceColl
import com.camelsoft.trademonitor._data.storage.entities.REPriceGoods

@Dao
interface IDaoPrice {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceColl(priceColl: REPriceColl)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceGoods(priceGoods: REPriceGoods)

    @Delete
    suspend fun deletePriceColl(priceColl: REPriceColl)

    @Delete
    suspend fun deletePriceGoods(priceGoods: REPriceGoods)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePriceColl(priceColl: REPriceColl)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePriceGoods(priceGoods: REPriceGoods)

    @Query("SELECT * FROM price_goods WHERE id_coll = :id_coll")
    suspend fun getPriceGoodes(id_coll: Long): List<REPriceGoods>
}