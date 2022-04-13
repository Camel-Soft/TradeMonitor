package com.camelsoft.trademonitor.common.di

import android.app.Application
import androidx.room.Room
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._data.storage.room.RoomDataBase
import com.camelsoft.trademonitor._data.storage.room.RoomImpl
import com.camelsoft.trademonitor.common.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModuleSingl {

    @Provides
    @Singleton
    fun provideStorageDataBase(app: Application): RoomDataBase {
        return Room.databaseBuilder(
            app,
            RoomDataBase::class.java,
            "storage_db_room"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRoom(db: RoomDataBase): IRoom {
        return RoomImpl(db.getDaoRoom())
    }

    @Provides
    @Singleton
    fun provideSettings(): Settings {
        return Settings()
    }
}