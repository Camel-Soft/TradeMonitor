package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._data.net.api.ISsl
import com.camelsoft.trademonitor._data.net.api.retro.*
import com.camelsoft.trademonitor._data.net.interceptors.TokenInterceptor
import com.camelsoft.trademonitor._data.net.servers.RetroLoc
import com.camelsoft.trademonitor._data.net.servers.RetroMy
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.api.offl_dbf.*
import com.camelsoft.trademonitor._domain.use_cases.use_cases_export.*
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.*
import com.camelsoft.trademonitor._domain.libs.ExportExcelSheet
import com.camelsoft.trademonitor._domain.libs.ExportJsonGoodes
import com.camelsoft.trademonitor._domain.libs.ExportJsonMarks
import com.camelsoft.trademonitor._domain.libs.ExportSouthRevision
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.HelloImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.InnImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.ObjectImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.SignImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.UseCaseRepoGoodsBigImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.UseCaseRepoGoodsOfflImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.UseCaseRepoInSouthUploadImpl
import com.camelsoft.trademonitor._presentation.api.ISign
import com.camelsoft.trademonitor._presentation.api.repo.*
import com.camelsoft.trademonitor.common.settings.Settings
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
    fun provideRetroMy(iSsl: ISsl, tokenInterceptor: TokenInterceptor, settings: Settings): RetroMy {
        return RetroMy(iSsl = iSsl, tokenInterceptor = tokenInterceptor, settings = settings)
    }

    @Provides
    @ViewModelScoped
    fun provideRetroLoc(iSsl: ISsl, tokenInterceptor: TokenInterceptor, settings: Settings): RetroLoc {
        return RetroLoc(iSsl = iSsl, tokenInterceptor = tokenInterceptor, settings = settings)
    }

    @Provides
    @ViewModelScoped
    fun provideNetApiSign(retroMy: RetroMy): NetApiSign {
        return retroMy.makeRetrofit().create(NetApiSign::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideSign(netApiSign: NetApiSign): ISign = SignImpl(netApiSign = netApiSign)

    @Provides
    @ViewModelScoped
    fun provideNetApiScan(retroMy: RetroMy, retroLoc: RetroLoc, settings: Settings): NetApiScan {
        if (settings.getConnSrvLoc().isBlank())
            return retroMy.makeRetrofit().create(NetApiScan::class.java)
        else
            return retroLoc.makeRetrofit().create(NetApiScan::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideNetApiHello(retroMy: RetroMy, retroLoc: RetroLoc, settings: Settings): NetApiHello {
        if (settings.getConnSrvLoc().isBlank())
            return retroMy.makeRetrofit().create(NetApiHello::class.java)
        else
            return retroLoc.makeRetrofit().create(NetApiHello::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideNetApiInSouthUpload(retroLoc: RetroLoc): NetApiInSouthUpload {
        return retroLoc.makeRetrofit().create(NetApiInSouthUpload::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideNetApiObject(retroLoc: RetroLoc): NetApiObject {
        return retroLoc.makeRetrofit().create(NetApiObject::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideNetApiInn(retroLoc: RetroLoc): NetApiInn {
        return retroLoc.makeRetrofit().create(NetApiInn::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideUseCaseRepoGoodsBigImpl(
        netApiScan: NetApiScan,
        settings: Settings,
        iSearchOnArtkl: ISearchOnArtkl,
        iSearchOnFirm: ISearchOnFirm,
        iSearchOnGrt: ISearchOnGrt,
        iSearchOnPrice: ISearchOnPrice,
        iSearchOnScan: ISearchOnScan,
        iSearchOnSgrt: ISearchOnSgrt
    ): IGoods {
        if (!settings.getWorkModeOffline())
            return UseCaseRepoGoodsBigImpl(netApiScan = netApiScan)
        else
            return UseCaseRepoGoodsOfflImpl(
                settings = settings,
                iSearchOnArtkl = iSearchOnArtkl,
                iSearchOnFirm = iSearchOnFirm,
                iSearchOnGrt = iSearchOnGrt,
                iSearchOnPrice = iSearchOnPrice,
                iSearchOnScan = iSearchOnScan,
                iSearchOnSgrt = iSearchOnSgrt
            )
    }

    @Provides
    @ViewModelScoped
    fun provideHello(netApiHello: NetApiHello): IHello {
        return HelloImpl(netApiHello = netApiHello)
    }

    @Provides
    @ViewModelScoped
    fun provideInSouthUpload(netApiInSouthUpload: NetApiInSouthUpload): IInSouthUpload {
        return UseCaseRepoInSouthUploadImpl(netApiInSouthUpload = netApiInSouthUpload)
    }

    @Provides
    @ViewModelScoped
    fun provideObject(netApiObject: NetApiObject): IObject {
        return ObjectImpl(netApiObject = netApiObject)
    }

    @Provides
    @ViewModelScoped
    fun provideInn(netApiInn: NetApiInn): IInn {
        return InnImpl(netApiInn = netApiInn)
    }
}
