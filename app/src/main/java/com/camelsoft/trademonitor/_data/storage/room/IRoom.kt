package com.camelsoft.trademonitor._data.storage.room

import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceGoods

interface IRoom {

    // Insert
    suspend fun insertRoomColl(priceColl: MPriceColl)
    suspend fun insertRoomGoods(priceGoods: MPriceGoods)

    // Delete
    suspend fun deleteRoomColl(priceColl: MPriceColl)
    suspend fun deleteRoomGoods(priceGoods: MPriceGoods)

    // Update
    suspend fun updateRoomColl(priceColl: MPriceColl)
    suspend fun updateRoomGoods(priceGoods: MPriceGoods)

    // Query
    suspend fun getRoomCollAll(): List<MPriceColl>
    suspend fun getRoomGoodes(id_coll: Long): List<MPriceGoods>
    suspend fun getRoomRightGoods(id_coll: Long, scancode: String): List<MPriceGoods>
}