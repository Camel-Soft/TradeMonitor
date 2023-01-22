package com.camelsoft.trademonitor._domain.use_cases.use_cases_repository

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.api.offl_dbf.*
import com.camelsoft.trademonitor._presentation.api.repo.IGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor._presentation.utils.addSep
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.Constants.Companion.ARTKL_DBF
import com.camelsoft.trademonitor.common.Constants.Companion.ARTKL_NDX
import com.camelsoft.trademonitor.common.Constants.Companion.FIRM_DBF
import com.camelsoft.trademonitor.common.Constants.Companion.FIRM_NDX
import com.camelsoft.trademonitor.common.Constants.Companion.GRT_DBF
import com.camelsoft.trademonitor.common.Constants.Companion.GRT_NDX
import com.camelsoft.trademonitor.common.Constants.Companion.PRICE_DBF
import com.camelsoft.trademonitor.common.Constants.Companion.PRICE_NDX
import com.camelsoft.trademonitor.common.Constants.Companion.SCAN_DBF
import com.camelsoft.trademonitor.common.Constants.Companion.SCAN_NDX
import com.camelsoft.trademonitor.common.Constants.Companion.SGRT_DBF
import com.camelsoft.trademonitor.common.Constants.Companion.SGRT_NDX
import com.camelsoft.trademonitor.common.settings.Settings

class UseCaseRepoGoodsOfflImpl(
    private val settings: Settings,
    private val iSearchOnArtkl: ISearchOnArtkl,
    private val iSearchOnFirm: ISearchOnFirm,
    private val iSearchOnGrt: ISearchOnGrt,
    private val iSearchOnPrice: ISearchOnPrice,
    private val iSearchOnScan: ISearchOnScan,
    private val iSearchOnSgrt: ISearchOnSgrt
    ): IGoods {

    private val scanBaseName = settings.getOfflBaseFolderName().absolutePath.addSep()+SCAN_DBF
    private val scanIndexName = settings.getOfflBaseFolderName().absolutePath.addSep()+SCAN_NDX
    private val priceBaseName = settings.getOfflBaseFolderName().absolutePath.addSep()+PRICE_DBF
    private val priceIndexName = settings.getOfflBaseFolderName().absolutePath.addSep()+PRICE_NDX
    private val artklBaseName = settings.getOfflBaseFolderName().absolutePath.addSep()+ARTKL_DBF
    private val artklIndexName = settings.getOfflBaseFolderName().absolutePath.addSep()+ARTKL_NDX
    private val grtBaseName = settings.getOfflBaseFolderName().absolutePath.addSep()+GRT_DBF
    private val grtIndexName = settings.getOfflBaseFolderName().absolutePath.addSep()+GRT_NDX
    private val sgrtBaseName = settings.getOfflBaseFolderName().absolutePath.addSep()+SGRT_DBF
    private val sgrtIndexName = settings.getOfflBaseFolderName().absolutePath.addSep()+SGRT_NDX
    private val firmBaseName = settings.getOfflBaseFolderName().absolutePath.addSep()+FIRM_DBF
    private val firmIndexName = settings.getOfflBaseFolderName().absolutePath.addSep()+FIRM_NDX

    override suspend fun getGoodsBig(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (settings.getMOffline().status > 0)
                return EventsGoods.Update(getAppContext().resources.getString(R.string.database_update))
            if (settings.getMOffline().status < 0)
                return EventsGoods.Error(getAppContext().resources.getString(R.string.error_offlbase_oops))

            val initPrice: Boolean
            val initArtkl: Boolean
            val initGrt: Boolean
            val initSgrt: Boolean
            val initFirm: Boolean
            if (!iSearchOnScan.openScan(scanBaseName, scanIndexName))
                return EventsGoods.Info(getAppContext().resources.getString(R.string.error_offlbase_not_ready))
            if (iSearchOnPrice.openPrice(priceBaseName, priceIndexName)) initPrice = true else initPrice = false
            if (iSearchOnArtkl.openArtkl(artklBaseName, artklIndexName)) initArtkl = true else initArtkl = false
            if (iSearchOnGrt.openGrt(grtBaseName, grtIndexName)) initGrt = true else initGrt = false
            if (iSearchOnSgrt.openSgrt(sgrtBaseName, sgrtIndexName)) initSgrt = true else initSgrt = false
            if (iSearchOnFirm.openFirm(firmBaseName, firmIndexName)) initFirm = true else initFirm = false

            var result: MGoodsBig = mGoodsBig

            when (val scanAnswer: EventsGoods<MGoodsBig> = iSearchOnScan.searchScan(mGoodsBig = result)) {
                is EventsGoods.Success -> result = scanAnswer.data
                is EventsGoods.UnSuccess -> return EventsGoods.UnSuccess(scanAnswer.message)
                is EventsGoods.Error -> return EventsGoods.Error(scanAnswer.message)
                is EventsGoods.Info -> return EventsGoods.Info(scanAnswer.message)
                is EventsGoods.Update -> return EventsGoods.Update(scanAnswer.message)
            }

            if (initPrice) {
                when (val priceAnswer: EventsGoods<MGoodsBig> = iSearchOnPrice.searchPrice(mGoodsBig = result)) {
                    is EventsGoods.Success -> result = priceAnswer.data
                    is EventsGoods.UnSuccess -> {}
                    is EventsGoods.Error -> {}
                    is EventsGoods.Info -> {}
                    is EventsGoods.Update -> {}
                }
            }

            if (initArtkl) {
                when (val artklAnswer: EventsGoods<MGoodsBig> = iSearchOnArtkl.searchArtkl(mGoodsBig = result)) {
                    is EventsGoods.Success -> result = artklAnswer.data
                    is EventsGoods.UnSuccess -> {}
                    is EventsGoods.Error -> {}
                    is EventsGoods.Info -> {}
                    is EventsGoods.Update -> {}
                }
            }

            if (initSgrt) {
                when (val sgrtAnswer: EventsGoods<MGoodsBig> = iSearchOnSgrt.searchSgrt(mGoodsBig = result)) {
                    is EventsGoods.Success -> result = sgrtAnswer.data
                    is EventsGoods.UnSuccess -> {}
                    is EventsGoods.Error -> {}
                    is EventsGoods.Info -> {}
                    is EventsGoods.Update -> {}
                }
            }

            if (initGrt) {
                when (val grtAnswer: EventsGoods<MGoodsBig> = iSearchOnGrt.searchGrt(mGoodsBig = result)) {
                    is EventsGoods.Success -> result = grtAnswer.data
                    is EventsGoods.UnSuccess -> {}
                    is EventsGoods.Error -> {}
                    is EventsGoods.Info -> {}
                    is EventsGoods.Update -> {}
                }
            }

            if (initFirm) {
                when (val firmAnswer: EventsGoods<MGoodsBig> = iSearchOnFirm.searchFirm(mGoodsBig = result)) {
                    is EventsGoods.Success -> result = firmAnswer.data
                    is EventsGoods.UnSuccess -> {}
                    is EventsGoods.Error -> {}
                    is EventsGoods.Info -> {}
                    is EventsGoods.Update -> {}
                }
            }

            iSearchOnScan.closeScan(scanBaseName)
            iSearchOnPrice.closePrice(priceBaseName)
            iSearchOnArtkl.closeArtkl(artklBaseName)
            iSearchOnGrt.closeGrt(grtBaseName)
            iSearchOnSgrt.closeSgrt(sgrtBaseName)
            iSearchOnFirm.closeFirm(firmBaseName)

            return EventsGoods.Success(result)
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[UseCaseRepoGoodsOfflImpl.getGoodsBig] ${e.localizedMessage}")
        }
    }
}
