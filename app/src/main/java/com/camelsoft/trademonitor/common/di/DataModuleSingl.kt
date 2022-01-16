package com.camelsoft.trademonitor.common.di

import android.app.Application
import androidx.room.Room
import com.camelsoft.trademonitor._data.storage.IPrice
import com.camelsoft.trademonitor._data.storage.PriceImpl
import com.camelsoft.trademonitor._data.storage.StorageDataBase
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
    fun provideStorageDataBase(app: Application): StorageDataBase {
        return Room.databaseBuilder(
            app,
            StorageDataBase::class.java,
            "storage_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePrice(db: StorageDataBase): IPrice {
        return PriceImpl(db.getDaoPrice())
    }
}