package com.camelsoft.trademonitor._data.storage.room

import com.camelsoft.trademonitor._presentation.models.MAlkoColl
import com.camelsoft.trademonitor._presentation.models.MAlkoMark
import com.camelsoft.trademonitor._presentation.models.MPriceColl
import com.camelsoft.trademonitor._presentation.models.MPriceGoods

interface IRoom {

    // Insert Price
    suspend fun insertRoomColl(priceColl: MPriceColl)
    suspend fun insertRoomGoods(priceGoods: MPriceGoods)

    // Delete Price
    suspend fun deleteRoomColl(priceColl: MPriceColl)
    suspend fun deleteRoomGoods(priceGoods: MPriceGoods)

    // Update Price
    suspend fun updateRoomColl(priceColl: MPriceColl)
    suspend fun updateRoomGoods(priceGoods: MPriceGoods)

    // Query Price
    suspend fun getRoomCollAll(): List<MPriceColl>
    suspend fun getRoomGoodes(id_coll: Long): List<MPriceGoods>
    suspend fun getRoomRightGoods(id_coll: Long, scancode: String): List<MPriceGoods>

    // Insert Alko
    suspend fun insertRoomAlkoColl(alkoColl: MAlkoColl)
    suspend fun insertRoomAlkoMark(alkoMark: MAlkoMark)

    // Delete Alko
    suspend fun deleteRoomAlkoColl(alkoColl: MAlkoColl)
    suspend fun deleteRoomAlkoMark(alkoMark: MAlkoMark)

    // Update Alko
    suspend fun updateRoomAlkoColl(alkoColl: MAlkoColl)
    suspend fun updateRoomAlkoMark(alkoMark: MAlkoMark)

    // Query Alko
    suspend fun getRoomAlkoCollAll(): List<MAlkoColl>
    suspend fun getRoomAlkoMarks(id_coll: Long): List<MAlkoMark>
    suspend fun getRoomAlkoMarkScan(id_coll: Long, scancode: String): List<MAlkoMark>
    suspend fun getRoomAlkoMarkMark(id_coll: Long, marka: String): List<MAlkoMark>
}