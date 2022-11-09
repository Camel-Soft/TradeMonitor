package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._data.net.managers.TokenManager
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.api.ITokenUser
import com.camelsoft.trademonitor._domain.api.ITokenVerifier
import com.camelsoft.trademonitor._domain.use_cases.use_cases_chzn.UseCaseChZnParamImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.*
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor._domain.libs.ExportExcelSheet
import com.camelsoft.trademonitor._domain.libs.ExportJsonGoodes
import com.camelsoft.trademonitor._domain.libs.ExportJsonMarks
import com.camelsoft.trademonitor._domain.libs.ExportSouthRevision
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenVerifierImpl
import com.camelsoft.trademonitor._presentation.api.IChZnParam
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

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoCollDelete(iRoom: IRoom): UseCaseStorageAlkoCollDelete {
        return UseCaseStorageAlkoCollDelete(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoCollGetAll(iRoom: IRoom): UseCaseStorageAlkoCollGetAll {
        return UseCaseStorageAlkoCollGetAll(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoCollInsert(iRoom: IRoom): UseCaseStorageAlkoCollInsert {
        return UseCaseStorageAlkoCollInsert(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoCollUpdate(iRoom: IRoom): UseCaseStorageAlkoCollUpdate {
        return UseCaseStorageAlkoCollUpdate(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoMarkDelete(iRoom: IRoom): UseCaseStorageAlkoMarkDelete {
        return UseCaseStorageAlkoMarkDelete(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoMarkGetAll(iRoom: IRoom): UseCaseStorageAlkoMarkGetAll {
        return UseCaseStorageAlkoMarkGetAll(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoMarkInsertOrUpdate(iRoom: IRoom): UseCaseStorageAlkoMarkInsertOrUpdate {
        return UseCaseStorageAlkoMarkInsertOrUpdate(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseStorageAlkoMarkUpdate(iRoom: IRoom): UseCaseStorageAlkoMarkUpdate {
        return UseCaseStorageAlkoMarkUpdate(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseExportExcelMarks(iRoom: IRoom, exportExcelSheet: ExportExcelSheet): UseCaseExportExcelMarks {
        return UseCaseExportExcelMarks(iRoom = iRoom, exportExcelSheet = exportExcelSheet)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseExportJsonMarks(iRoom: IRoom, exportJsonMarks: ExportJsonMarks): UseCaseExportJsonMarks {
        return UseCaseExportJsonMarks(iRoom = iRoom, exportJsonMarks = exportJsonMarks)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseExpChZnMilkWithdrawal(iRoom: IRoom): UseCaseExpChZnMilkWithdrawal {
        return UseCaseExpChZnMilkWithdrawal(iRoom = iRoom)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseChZnParamImpl(): IChZnParam {
        return UseCaseChZnParamImpl()
    }

    @Provides
    @ViewModelScoped
    fun provideTokenVerifier(tokenUser: ITokenUser, tokenManager: TokenManager): ITokenVerifier {
        return TokenVerifierImpl(tokenUser = tokenUser, tokenManager = tokenManager)
    }
}
