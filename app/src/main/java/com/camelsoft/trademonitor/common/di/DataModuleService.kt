package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._data.net.api.ISsl
import com.camelsoft.trademonitor._data.net.api.retro.NetApiOfflBase
import com.camelsoft.trademonitor._data.net.interceptors.TokenInterceptor
import com.camelsoft.trademonitor._data.net.servers.RetroLoc
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.UseCaseRepoOfflBaseImpl
import com.camelsoft.trademonitor._presentation.api.repo.IOfflBase
import com.camelsoft.trademonitor.common.settings.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object DataModuleService {

    @Provides
    @ServiceScoped
    fun provideNetApiOfflBase(settings: Settings, iSsl: ISsl, tokenInterceptor: TokenInterceptor): NetApiOfflBase {
        val retroLoc = RetroLoc(iSsl = iSsl, tokenInterceptor = tokenInterceptor, settings = settings)
        return retroLoc.makeRetrofit().create(NetApiOfflBase::class.java)
    }

    @Provides
    @ServiceScoped
    fun provideOfflBase(netApiOfflBase: NetApiOfflBase, settings: Settings): IOfflBase {
        return UseCaseRepoOfflBaseImpl(netApiOfflBase = netApiOfflBase, settings = settings)
    }
}
