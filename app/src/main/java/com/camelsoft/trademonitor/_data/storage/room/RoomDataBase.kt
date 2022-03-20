package com.camelsoft.trademonitor._data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomColl
import com.camelsoft.trademonitor._data.storage.room.entities.ERoomGoods

@Database(
    version = 1,
    entities = [
        ERoomColl::class,
        ERoomGoods::class
    ]
)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun getDaoRoom(): IDaoRoom
}