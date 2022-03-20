package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollDelete
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollGetAll
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollInsert
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageCollUpdate
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
}