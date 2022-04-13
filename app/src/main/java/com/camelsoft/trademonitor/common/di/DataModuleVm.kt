package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor.common.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DataModuleVm {

    @Provides
    fun provideUseCaseStorageCollDelete(iRoom: IRoom): UseCaseStorageCollDelete {
        return UseCaseStorageCollDelete(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageCollInsert(iRoom: IRoom): UseCaseStorageCollInsert {
        return UseCaseStorageCollInsert(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageCollUpdate(iRoom: IRoom): UseCaseStorageCollUpdate {
        return UseCaseStorageCollUpdate(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageCollGetAll(iRoom: IRoom): UseCaseStorageCollGetAll {
        return UseCaseStorageCollGetAll(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageGoodsDelete(iRoom: IRoom): UseCaseStorageGoodsDelete {
        return UseCaseStorageGoodsDelete(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageGoodsInsert(iRoom: IRoom): UseCaseStorageGoodsInsert {
        return UseCaseStorageGoodsInsert(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageGoodsUpdate(iRoom: IRoom): UseCaseStorageGoodsUpdate {
        return UseCaseStorageGoodsUpdate(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageGoodsGetAll(iRoom: IRoom): UseCaseStorageGoodsGetAll {
        return UseCaseStorageGoodsGetAll(iRoom = iRoom)
    }

    @Provides
    fun provideUseCaseStorageGoodsInsertOrUpdate(iRoom: IRoom, settings: Settings): UseCaseStorageGoodsInsertOrUpdate {
        return UseCaseStorageGoodsInsertOrUpdate(iRoom = iRoom, settings = settings)
    }
}