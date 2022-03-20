package com.camelsoft.trademonitor._data.storage.room

import androidx.room.*
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomGoods

@Dao
interface IDaoRoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomColl(roomColl: ERoomColl)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomGoods(roomGoods: ERoomGoods)

    @Delete
    suspend fun deleteRoomColl(roomColl: ERoomColl)

    @Delete
    suspend fun deleteRoomGoods(roomGoods: ERoomGoods)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoomColl(roomColl: ERoomColl)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoomGoods(roomGoods: ERoomGoods)

    @Query("SELECT * FROM room_collections")
    suspend fun getRoomCollAll(): List<ERoomColl>

    @Query("SELECT * FROM room_goods WHERE id_coll = :id_coll")
    suspend fun getRoomGoodes(id_coll: Long): List<ERoomGoods>
}