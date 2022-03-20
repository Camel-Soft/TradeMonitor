package com.camelsoft.trademonitor._data.storage.room

import com.camelsoft.trademonitor._data.storage.room.entities.ERoomColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomGoods
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceGoods

class RoomImpl(private val daoRoom: IDaoRoom): IRoom {

    override suspend fun insertRoomColl(priceColl: MPriceColl) {
        daoRoom.insertRoomColl(ERoomColl.toERoomColl(priceColl))
    }

    override suspend fun insertRoomGoods(priceGoods: MPriceGoods) {
        daoRoom.insertRoomGoods(ERoomGoods.toERoomGoods(priceGoods))
    }

    override suspend fun deleteRoomColl(priceColl: MPriceColl) {
        daoRoom.deleteRoomColl(ERoomColl.toERoomColl(priceColl))
    }

    override suspend fun deleteRoomGoods(priceGoods: MPriceGoods) {
        daoRoom.deleteRoomGoods(ERoomGoods.toERoomGoods(priceGoods))
    }

    override suspend fun updateRoomColl(priceColl: MPriceColl) {
        daoRoom.updateRoomColl(ERoomColl.toERoomColl(priceColl))
    }

    override suspend fun updateRoomGoods(priceGoods: MPriceGoods) {
        daoRoom.updateRoomGoods(ERoomGoods.toERoomGoods(priceGoods))
    }

    override suspend fun getRoomCollAll(): List<MPriceColl> {
        return daoRoom.getRoomCollAll().map { it.toMPriceColl() }

    }

    override suspend fun getRoomGoodes(id_coll: Long): List<MPriceGoods> {
        return daoRoom.getRoomGoodes(id_coll).map { it.toMPriceGoods() }
    }
}