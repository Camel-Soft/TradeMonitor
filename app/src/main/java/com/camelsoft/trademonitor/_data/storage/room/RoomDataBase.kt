package com.camelsoft.trademonitor._data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomAlkoColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomAlkoMark
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomGoods

@Database(
    entities = [
        ERoomColl::class,
        ERoomGoods::class,
        ERoomAlkoColl::class,
        ERoomAlkoMark::class
    ],
    version = 1,
    exportSchema = true
)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun getDaoRoom(): IDaoRoom
}