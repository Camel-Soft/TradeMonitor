package com.camelsoft.trademonitor._data.storage.dbf

import com.camelsoft.trademonitor._domain.api.offl_dbf.ISearchOnFirm
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import org.xBaseJ.DBF
import org.xBaseJ.fields.CharField
import java.io.File

class SearchOnFirmDbfImpl : ISearchOnFirm {
    private var dbInit = false
    private lateinit var dbFirm: DBF

    override suspend fun openFirm(firmBaseName: String, firmIndexName: String?): Boolean {
        try {
            val firmBaseFile = File(firmBaseName)
            if (!firmBaseFile.exists() || !firmBaseFile.isFile) {
                dbInit = false
                if (this::dbFirm.isInitialized) dbFirm.close()
                return false
            }
            if (firmIndexName == null) {
                dbInit = false
                if (this::dbFirm.isInitialized) dbFirm.close()
                return false
            }
            else {
                val firmIndexFile = File(firmIndexName)
                if (!firmIndexFile.exists() || !firmIndexFile.isFile) {
                    dbInit = false
                    if (this::dbFirm.isInitialized) dbFirm.close()
                    return false
                }
                dbFirm = DBF(firmBaseName, "IBM866")
                dbFirm.useIndex(firmIndexName)
                dbInit = true
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            dbInit = false
            if (this::dbFirm.isInitialized) dbFirm.close()
            return false
        }
    }

    override suspend fun searchFirm(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (!dbInit) {
                return EventsGoods.Error("[SearchOnFirmDbfImpl.searchFirm] Подготовка баз данных для поиска не проведена")
            }

            val sklCod: String = mGoodsBig.sklad.trim()
            if (sklCod.isEmpty()) {
                return EventsGoods.Error("[SearchOnFirmDbfImpl.searchFirm] Код склада для поиска не определен")
            }

            val result: MGoodsBig = mGoodsBig

            val sklName: CharField = dbFirm.getField("NAME") as CharField

            // Поиск
            if (dbFirm.find(sklCod)) {
                result.sklad = sklName.get().trim()
                return EventsGoods.Success(data = result)
            }

            return EventsGoods.UnSuccess("Название склада по коду склада не найдено")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[SearchOnFirmDbfImpl.searchFirm] ${e.localizedMessage}")
        }
    }

    override suspend fun closeFirm(firmBaseName: String) {
        dbInit = false
        if (this::dbFirm.isInitialized) dbFirm.close()
    }
}
