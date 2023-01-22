package com.camelsoft.trademonitor._data.storage.dbf

import com.camelsoft.trademonitor._domain.api.offl_dbf.ISearchOnGrt
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import org.xBaseJ.DBF
import org.xBaseJ.fields.CharField
import java.io.File

/*
    Поиск сначала нужно осуществлять по подгруппе (sgrt), а потом по группе (grt).
    Это связано с замещением значения поля группы (grt) в результирующем MGoods
 */

class SearchOnGrtDbfImpl : ISearchOnGrt {
    private var dbInit = false
    private lateinit var dbGrt: DBF

    override suspend fun openGrt(grtBaseName: String, grtIndexName: String?): Boolean {
        try {
            val grtBaseFile = File(grtBaseName)
            if (!grtBaseFile.exists() || !grtBaseFile.isFile) {
                dbInit = false
                if (this::dbGrt.isInitialized) dbGrt.close()
                return false
            }
            if (grtIndexName == null) {
                dbInit = false
                if (this::dbGrt.isInitialized) dbGrt.close()
                return false
            }
            else {
                val grtIndexFile = File(grtIndexName)
                if (!grtIndexFile.exists() || !grtIndexFile.isFile) {
                    dbInit = false
                    if (this::dbGrt.isInitialized) dbGrt.close()
                    return false
                }
                dbGrt = DBF(grtBaseName, "IBM866")
                dbGrt.useIndex(grtIndexName)
                dbInit = true
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            dbInit = false
            if (this::dbGrt.isInitialized) dbGrt.close()
            return false
        }
    }

    override suspend fun searchGrt(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (!dbInit) {
                return EventsGoods.Error("[SearchOnGrtDbfImpl.searchGrt] Подготовка баз данных для поиска не проведена")
            }

            val grtCod: String = mGoodsBig.grt.trim()
            if (grtCod.isEmpty()) {
                return EventsGoods.Error("[SearchOnGrtDbfImpl.searchGrt] Код группы для поиска не определен")
            }

            val result: MGoodsBig = mGoodsBig

            val grtName: CharField = dbGrt.getField("NAME") as CharField

            // Поиск
            if (dbGrt.find(grtCod)) {
                result.grt = grtName.get().trim()
                return EventsGoods.Success(data = result)
            }

            return EventsGoods.UnSuccess("Название группы по коду группы не найдено")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[SearchOnGrtDbfImpl.searchGrt] ${e.localizedMessage}")
        }
    }

    override suspend fun closeGrt(grtBaseName: String) {
        dbInit = false
        if (this::dbGrt.isInitialized) dbGrt.close()
    }
}
