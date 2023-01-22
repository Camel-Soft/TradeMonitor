package com.camelsoft.trademonitor._data.storage.dbf

import com.camelsoft.trademonitor._domain.api.offl_dbf.ISearchOnSgrt
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import org.xBaseJ.DBF
import org.xBaseJ.fields.CharField
import java.io.File

/*
    Поиск сначала нужно осуществлять по подгруппе (sgrt), а потом по группе (grt).
    Это связано с замещением значения поля группы (grt) в результирующем MGoods
 */

class SearchOnSgrtDbfImpl : ISearchOnSgrt {
    private var dbInit = false
    private lateinit var dbSgrt: DBF

    override suspend fun openSgrt(sgrtBaseName: String, sgrtIndexName: String?): Boolean {
        try {
            val sgrtBaseFile = File(sgrtBaseName)
            if (!sgrtBaseFile.exists() || !sgrtBaseFile.isFile) {
                dbInit = false
                if (this::dbSgrt.isInitialized) dbSgrt.close()
                return false
            }
            if (sgrtIndexName == null) {
                dbInit = false
                if (this::dbSgrt.isInitialized) dbSgrt.close()
                return false
            }
            else {
                val sgrtIndexFile = File(sgrtIndexName)
                if (!sgrtIndexFile.exists() || !sgrtIndexFile.isFile) {
                    dbInit = false
                    if (this::dbSgrt.isInitialized) dbSgrt.close()
                    return false
                }
                dbSgrt = DBF(sgrtBaseName, "IBM866")
                dbSgrt.useIndex(sgrtIndexName)
                dbInit = true
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            dbInit = false
            if (this::dbSgrt.isInitialized) dbSgrt.close()
            return false
        }
    }

    override suspend fun searchSgrt(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (!dbInit) {
                return EventsGoods.Error("[SearchOnSgrtDbfImpl.searchSgrt] Подготовка баз данных для поиска не проведена")
            }

            val mGoodsGrt: String = mGoodsBig.grt.trim()
            val mGoodsSgrt: String = mGoodsBig.sgrt.trim()
            if (mGoodsGrt.isBlank() || mGoodsSgrt.isBlank()) {
                return EventsGoods.Error("[SearchOnSgrtDbfImpl.searchSgrt] Код группы/подгруппы для поиска не определен")
            }

            val result: MGoodsBig = mGoodsBig

            val sgrtCod: CharField = dbSgrt.getField("COD_SGRT") as CharField
            val sgrtName: CharField = dbSgrt.getField("NAME") as CharField
            val sgrtManager: CharField = dbSgrt.getField("MANAGER") as CharField

            // Поиск
            if (dbSgrt.find(mGoodsGrt)) {
                if (mGoodsSgrt == sgrtCod.get()) {
                    result.sgrt = sgrtName.get().trim()
                    result.rezerv1 = sgrtManager.get().trim()
                    return EventsGoods.Success(data = result)
                }
                else {
                    var recNo: Int = dbSgrt.currentRecordNumber
                    while (recNo < dbSgrt.recordCount) {
                        try { dbSgrt.findNext() } catch (e: Exception) { break }
                        if (mGoodsSgrt == sgrtCod.get()) {
                            result.sgrt = sgrtName.get().trim()
                            result.rezerv1 = sgrtManager.get().trim()
                            return EventsGoods.Success(data = result)
                        }
                        recNo = dbSgrt.currentRecordNumber
                    }
                }
            }

            return EventsGoods.UnSuccess("Название подгруппы по кодам группы/подгруппы не найдено")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[SearchOnSgrtDbfImpl.searchSgrt] ${e.localizedMessage}")
        }
    }

    override suspend fun closeSgrt(sgrtBaseName: String) {
        dbInit = false
        if (this::dbSgrt.isInitialized) dbSgrt.close()
    }
}
