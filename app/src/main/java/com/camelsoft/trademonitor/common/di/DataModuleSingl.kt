package com.camelsoft.trademonitor.common.di

import android.app.Application
import androidx.room.Room
import com.camelsoft.trademonitor._data.net.api.ISsl
import com.camelsoft.trademonitor._data.net.api.retro.NetApiSign
import com.camelsoft.trademonitor._data.net.interceptors.TokenInterceptor
import com.camelsoft.trademonitor._data.net.managers.TokenManager
import com.camelsoft.trademonitor._data.net.servers.RetroMy
import com.camelsoft.trademonitor._data.net.servers.SslImpl
import com.camelsoft.trademonitor._data.storage.dbf.*
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._data.storage.room.RoomDataBase
import com.camelsoft.trademonitor._data.storage.room.RoomImpl
import com.camelsoft.trademonitor._domain.api.ITelephony
import com.camelsoft.trademonitor._domain.api.ITokenUser
import com.camelsoft.trademonitor._domain.api.offl_dbf.*
import com.camelsoft.trademonitor._domain.libs.*
import com.camelsoft.trademonitor._domain.use_cases.use_cases_net.SignImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenUserImpl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_security.TokenUserVerifier
import com.camelsoft.trademonitor._presentation.api.ISign
import com.camelsoft.trademonitor._presentation.notifications.OfflineNotification
import com.camelsoft.trademonitor.common.settings.Settings
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
    fun provideRoom(db: RoomDataBase): IRoom = RoomImpl(db.getDaoRoom())

    @Provides
    @Singleton
    fun provideSettings(): Settings = Settings

    @Provides
    @Singleton
    fun provideExportExcelSheet(): ExportExcelSheet = ExportExcelSheet()

    @Provides
    @Singleton
    fun provideExportSouthRevision(): ExportSouthRevision = ExportSouthRevision()

    @Provides
    @Singleton
    fun provideExportJsonGoodes(): ExportJsonGoodes = ExportJsonGoodes()

    @Provides
    @Singleton
    fun provideExportJsonMarks(): ExportJsonMarks = ExportJsonMarks()

    @Provides
    @Singleton
    fun provideSsl(): ISsl = SslImpl()

    @Provides
    @Singleton
    fun provideTokenManager(): TokenManager = TokenManager

    @Provides
    @Singleton
    fun provideTokenInterceptor(tokenManager: TokenManager): TokenInterceptor {
        return TokenInterceptor(tokenManager = tokenManager)
    }

    @Provides
    @Singleton
    fun provideRetroMy(iSsl: ISsl, tokenInterceptor: TokenInterceptor): RetroMy {
        return RetroMy(iSsl = iSsl, tokenInterceptor = tokenInterceptor)
    }

    @Provides
    @Singleton
    fun provideNetApiSign(retroMy: RetroMy): NetApiSign {
        return retroMy.makeRetrofit().create(NetApiSign::class.java)
    }

    @Provides
    @Singleton
    fun provideSign(netApiSign: NetApiSign): ISign = SignImpl(netApiSign = netApiSign)

    @Provides
    @Singleton
    fun provideTelephony(): ITelephony = TelephonyImpl()

    @Provides
    @Singleton
    fun provideTokenUser(telephony: ITelephony): ITokenUser = TokenUserImpl(telephony = telephony)

    @Provides
    @Singleton
    fun provideTokenUserVerifier(tokenUser: ITokenUser, tokenManager: TokenManager): TokenUserVerifier {
        return TokenUserVerifier(tokenUser = tokenUser, tokenManager = tokenManager)
    }

    @Provides
    @Singleton
    fun provideOfflineNotification(): OfflineNotification = OfflineNotification()

    @Provides
    @Singleton
    fun provideSearchOnArtkl(): ISearchOnArtkl = SearchOnArtklDbfImpl()

    @Provides
    @Singleton
    fun provideSearchOnFirml(): ISearchOnFirm = SearchOnFirmDbfImpl()

    @Provides
    @Singleton
    fun provideSearchOnGrt(): ISearchOnGrt = SearchOnGrtDbfImpl()

    @Provides
    @Singleton
    fun provideSearchOnPrice(): ISearchOnPrice = SearchOnPriceDbfImpl()

    @Provides
    @Singleton
    fun provideSearchOnScan(settings: Settings): ISearchOnScan = SearchOnScanDbfImpl(settings)

    @Provides
    @Singleton
    fun provideSearchOnSgrt(): ISearchOnSgrt = SearchOnSgrtDbfImpl()
}
