package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportExcelSheet
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportJsonGoodes
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.UseCaseExportSouthRevision
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor._domain.utils.ExportExcelSheet
import com.camelsoft.trademonitor._domain.utils.ExportJsonGoodes
import com.camelsoft.trademonitor._domain.utils.ExportSouthRevision
import com.camelsoft.trademonitor.common.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DataModuleVm {

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageCollDelete(iRoom: IRoom): UseCaseStorageCollDelete {
        return UseCaseStorageCollDelete(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageCollInsert(iRoom: IRoom): UseCaseStorageCollInsert {
        return UseCaseStorageCollInsert(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageCollUpdate(iRoom: IRoom): UseCaseStorageCollUpdate {
        return UseCaseStorageCollUpdate(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageCollGetAll(iRoom: IRoom): UseCaseStorageCollGetAll {
        return UseCaseStorageCollGetAll(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageGoodsDelete(iRoom: IRoom): UseCaseStorageGoodsDelete {
        return UseCaseStorageGoodsDelete(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageGoodsInsert(iRoom: IRoom): UseCaseStorageGoodsInsert {
        return UseCaseStorageGoodsInsert(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageGoodsUpdate(iRoom: IRoom): UseCaseStorageGoodsUpdate {
        return UseCaseStorageGoodsUpdate(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageGoodsGetAll(iRoom: IRoom): UseCaseStorageGoodsGetAll {
        return UseCaseStorageGoodsGetAll(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageGoodsInsertOrUpdate(iRoom: IRoom, settings: Settings): UseCaseStorageGoodsInsertOrUpdate {
        return UseCaseStorageGoodsInsertOrUpdate(iRoom = iRoom, settings = settings)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseExportExcelSheet(iRoom: IRoom, exportExcelSheet: ExportExcelSheet): UseCaseExportExcelSheet {
        return UseCaseExportExcelSheet(iRoom = iRoom, exportExcelSheet = exportExcelSheet)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseExportSouthRevision(iRoom: IRoom, exportSouthRevision: ExportSouthRevision): UseCaseExportSouthRevision {
        return UseCaseExportSouthRevision(iRoom = iRoom, exportSouthRevision = exportSouthRevision)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseExportJsonGoodes(iRoom: IRoom, exportJsonGoodes: ExportJsonGoodes): UseCaseExportJsonGoodes {
        return UseCaseExportJsonGoodes(iRoom = iRoom, exportJsonGoodes = exportJsonGoodes)
    }
}