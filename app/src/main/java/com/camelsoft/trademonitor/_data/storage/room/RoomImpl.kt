package com.camelsoft.trademonitor._data.storage.room

import com.camelsoft.trademonitor._data.storage.room.entities.ERoomAlkoColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomAlkoMark
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomGoods
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._presentation.models.price.MPriceGoods

class RoomImpl(private val daoRoom: IDaoRoom): IRoom {

    // Price
    override suspend fun insertRoomColl(priceColl: MPriceColl) {
        daoRoom.insertRoomColl(ERoomColl.toERoomColl(priceColl))
    }

    // Price
    override suspend fun insertRoomGoods(priceGoods: MPriceGoods) {
        daoRoom.insertRoomGoods(ERoomGoods.toERoomGoods(priceGoods))
    }

    // Price
    override suspend fun deleteRoomColl(priceColl: MPriceColl) {
        daoRoom.deleteRoomColl(ERoomColl.toERoomColl(priceColl))
    }

    // Price
    override suspend fun deleteRoomGoods(priceGoods: MPriceGoods) {
        daoRoom.deleteRoomGoods(ERoomGoods.toERoomGoods(priceGoods))
    }

    // Price
    override suspend fun updateRoomColl(priceColl: MPriceColl) {
        daoRoom.updateRoomColl(ERoomColl.toERoomColl(priceColl))
    }

    // Price
    override suspend fun updateRoomGoods(priceGoods: MPriceGoods) {
        daoRoom.updateRoomGoods(ERoomGoods.toERoomGoods(priceGoods))
    }

    // Price
    override suspend fun getRoomCollAll(): List<MPriceColl> {
        return daoRoom.getRoomCollAll().map { it.toMPriceColl() }
    }

    // Price
    override suspend fun getRoomGoodes(id_coll: Long): List<MPriceGoods> {
        return daoRoom.getRoomGoodes(id_coll).map { it.toMPriceGoods() }
    }

    // Price
    override suspend fun getRoomRightGoods(id_coll: Long, scancode: String): List<MPriceGoods> {
        return daoRoom.getRoomRightGoods(id_coll, scancode).map { it.toMPriceGoods() }
    }

    // Alko
    override suspend fun insertRoomAlkoColl(alkoColl: MAlkoColl) {
        daoRoom.insertRoomAlkoColl(ERoomAlkoColl.toERoomAlkoColl(alkoColl))
    }

    // Alko
    override suspend fun insertRoomAlkoMark(alkoMark: MAlkoMark) {
        daoRoom.insertRoomAlkoMark(ERoomAlkoMark.toERoomAlkoMark(alkoMark))
    }

    // Alko
    override suspend fun deleteRoomAlkoColl(alkoColl: MAlkoColl) {
        daoRoom.deleteRoomAlkoColl(ERoomAlkoColl.toERoomAlkoColl(alkoColl))
    }

    // Alko
    override suspend fun deleteRoomAlkoMark(alkoMark: MAlkoMark) {
        daoRoom.deleteRoomAlkoMark(ERoomAlkoMark.toERoomAlkoMark(alkoMark))
    }

    // Alko
    override suspend fun updateRoomAlkoColl(alkoColl: MAlkoColl) {
        daoRoom.updateRoomAlkoColl(ERoomAlkoColl.toERoomAlkoColl(alkoColl))
    }

    // Alko
    override suspend fun updateRoomAlkoMark(alkoMark: MAlkoMark) {
        daoRoom.updateRoomAlkoMark(ERoomAlkoMark.toERoomAlkoMark(alkoMark))
    }

    // Alko
    override suspend fun getRoomAlkoCollAll(): List<MAlkoColl> {
        return daoRoom.getRoomAlkoCollAll().map { it.toMAlkoColl() }
    }

    // Alko
    override suspend fun getRoomAlkoMarks(id_coll: Long): List<MAlkoMark> {
        return daoRoom.getRoomAlkoMarks(id_coll).map { it.toMAlkoMark() }
    }

    // Alko
    override suspend fun getRoomAlkoMarkScan(id_coll: Long, scancode: String): List<MAlkoMark> {
        return daoRoom.getRoomAlkoMarkScan(id_coll, scancode).map { it.toMAlkoMark() }
    }

    // Alko
    override suspend fun getRoomAlkoMarkMark(id_coll: Long, marka: String): List<MAlkoMark> {
        return daoRoom.getRoomAlkoMarkMark(id_coll, marka).map { it.toMAlkoMark() }
    }
}