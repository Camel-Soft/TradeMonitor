package com.camelsoft.trademonitor._data.storage.room

import androidx.room.*
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomAlkoColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomAlkoMark
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomGoods

@Dao
interface IDaoRoom {

    // Price
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomColl(roomColl: ERoomColl)

    // Price
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomGoods(roomGoods: ERoomGoods)

    // Price
    @Delete
    suspend fun deleteRoomColl(roomColl: ERoomColl)

    // Price
    @Delete
    suspend fun deleteRoomGoods(roomGoods: ERoomGoods)

    // Price
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoomColl(roomColl: ERoomColl)

    // Price
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoomGoods(roomGoods: ERoomGoods)

    // Price
    @Query("SELECT * FROM room_collections")
    suspend fun getRoomCollAll(): List<ERoomColl>

    // Price
    @Query("SELECT * FROM room_goods WHERE id_coll = :id_coll")
    suspend fun getRoomGoodes(id_coll: Long): List<ERoomGoods>

    // Price
    @Query("SELECT * FROM room_goods WHERE id_coll = :id_coll AND scancode = :scancode")
    suspend fun getRoomRightGoods(id_coll: Long, scancode: String): List<ERoomGoods>

    // Alko
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomAlkoColl(roomAlkoColl: ERoomAlkoColl)

    // Alko
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomAlkoMark(roomAlkoMark: ERoomAlkoMark)

    // Alko
    @Delete
    suspend fun deleteRoomAlkoColl(roomAlkoColl: ERoomAlkoColl)

    // Alko
    @Delete
    suspend fun deleteRoomAlkoMark(roomAlkoMark: ERoomAlkoMark)

    // Alko
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoomAlkoColl(roomAlkoColl: ERoomAlkoColl)

    // Alko
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRoomAlkoMark(roomAlkoMark: ERoomAlkoMark)

    // Alko
    @Query("SELECT * FROM room_collections_alko")
    suspend fun getRoomAlkoCollAll(): List<ERoomAlkoColl>

    // Alko
    @Query("SELECT * FROM room_mark_alko WHERE id_coll = :id_coll")
    suspend fun getRoomAlkoMarks(id_coll: Long): List<ERoomAlkoMark>

    // Alko
    @Query("SELECT * FROM room_mark_alko WHERE id_coll = :id_coll AND scancode = :scancode")
    suspend fun getRoomAlkoMarkScan(id_coll: Long, scancode: String): List<ERoomAlkoMark>

    // Alko
    @Query("SELECT * FROM room_mark_alko WHERE id_coll = :id_coll AND marka = :marka")
    suspend fun getRoomAlkoMarkMark(id_coll: Long, marka: String): List<ERoomAlkoMark>
}