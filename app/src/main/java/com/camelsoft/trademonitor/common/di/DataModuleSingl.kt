package com.camelsoft.trademonitor.common.di

import android.app.Application
import androidx.room.Room
import com.camelsoft.trademonitor._data.net.api.NetApiMy
import com.camelsoft.trademonitor._data.net.api.NetApiScan
import com.camelsoft.trademonitor._data.net.managers.TokenManager
import com.camelsoft.trademonitor._data.net.servers.RetroMy
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._data.storage.room.RoomDataBase
import com.camelsoft.trademonitor._data.storage.room.RoomImpl
import com.camelsoft.trademonitor._domain.api.ITelephony
import com.camelsoft.trademonitor._domain.api.ITokenUser
import com.camelsoft.trademonitor._domain.libs.*
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.UseCaseRepoGoodsBigImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenUserImpl
import com.camelsoft.trademonitor._presentation.api.IGoods
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

    @Provides
    @Singleton
    fun provideExportExcelSheet(): ExportExcelSheet {
        return ExportExcelSheet()
    }

    @Provides
    @Singleton
    fun provideExportSouthRevision(): ExportSouthRevision {
        return ExportSouthRevision()
    }

    @Provides
    @Singleton
    fun provideExportJsonGoodes(): ExportJsonGoodes {
        return ExportJsonGoodes()
    }

    @Provides
    @Singleton
    fun provideExportJsonMarks(): ExportJsonMarks {
        return ExportJsonMarks()
    }

    @Provides
    @Singleton
    fun provideRetroMy(): RetroMy {
        return RetroMy()
    }

    @Provides
    @Singleton
    fun provideNetApiScan(retroMy: RetroMy): NetApiScan {
        return retroMy.retrofit.create(NetApiScan::class.java)
    }

    @Provides
    @Singleton
    fun provideNetApiMy(retroMy: RetroMy): NetApiMy {
        return retroMy.retrofit.create(NetApiMy::class.java)
    }

    @Provides
    @Singleton
    fun provideUseCaseRepoGoodsBigImpl(netApiScan: NetApiScan): IGoods {
        return UseCaseRepoGoodsBigImpl(netApiScan)
    }

    @Provides
    @Singleton
    fun provideTokenManager(): TokenManager {
        return TokenManager
    }

    @Provides
    @Singleton
    fun provideTelephony(): ITelephony {
        return TelephonyImpl()
    }

    @Provides
    @Singleton
    fun provideTokenUser(telephony: ITelephony): ITokenUser {
        return TokenUserImpl(telephony = telephony)
    }
}
