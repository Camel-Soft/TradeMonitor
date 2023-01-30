package com.camelsoft.trademonitor.common.di

import com.camelsoft.trademonitor._presentation.api.scan.IScanner
import com.camelsoft.trademonitor._presentation.barcode_scanners.csi_moby_one.CsiMobyOneImpl
import com.camelsoft.trademonitor._presentation.barcode_scanners.empty_scanner.EmptyScannerImpl
import com.camelsoft.trademonitor._presentation.barcode_scanners.honeywell_eda50k.HoneywellEda50kImpl
import com.camelsoft.trademonitor.common.settings.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object DataModuleFragment {

    @Provides
    @FragmentScoped
    fun provideScanner(settings: Settings): IScanner {
        when (settings.getScanner()) {
            "honeywell_eda50k" -> return HoneywellEda50kImpl()
            "csi_moby_one" -> return CsiMobyOneImpl()
            else -> return EmptyScannerImpl()
        }
    }
}
