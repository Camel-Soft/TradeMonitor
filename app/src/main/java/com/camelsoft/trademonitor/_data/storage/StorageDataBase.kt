package com.camelsoft.trademonitor._data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.camelsoft.trademonitor._data.storage.entities.REPriceColl
import com.camelsoft.trademonitor._data.storage.entities.REPriceGoods

@Database(
    version = 1,
    entities = [
        REPriceColl::class,
        REPriceGoods::class
    ]
)
abstract class StorageDataBase : RoomDatabase() {
    abstract fun getDaoPrice(): IDaoPrice
}