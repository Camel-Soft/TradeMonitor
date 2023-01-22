package com.camelsoft.trademonitor._data.storage.dbf

import com.camelsoft.trademonitor._domain.api.offl_dbf.ISearchOnArtkl
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import org.xBaseJ.DBF
import org.xBaseJ.fields.CharField
import java.io.File

class SearchOnArtklDbfImpl : ISearchOnArtkl {
    private var dbInit = false
    private lateinit var dbArtkl: DBF

    override suspend fun openArtkl(artklBaseName: String, artklIndexName: String?): Boolean {
        try {
            val artklBaseFile = File(artklBaseName)
            if (!artklBaseFile.exists() || !artklBaseFile.isFile) {
                dbInit = false
                if (this::dbArtkl.isInitialized) dbArtkl.close()
                return false
            }
            if (artklIndexName == null) {
                dbInit = false
                if (this::dbArtkl.isInitialized) dbArtkl.close()
                return false
            }
            else {
                val artklIndexFile = File(artklIndexName)
                if (!artklIndexFile.exists() || !artklIndexFile.isFile) {
                    dbInit = false
                    if (this::dbArtkl.isInitialized) dbArtkl.close()
                    return false
                }
                dbArtkl = DBF(artklBaseName, "IBM866")
                dbArtkl.useIndex(artklIndexName)
                dbInit = true
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            dbInit = false
            if (this::dbArtkl.isInitialized) dbArtkl.close()
            return false
        }
    }

    override suspend fun searchArtkl(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (!dbInit) {
                return EventsGoods.Error("[SearchOnArtklDbfImpl.searchArtkl] Подготовка баз данных для поиска не проведена")
            }

            val cod: String = mGoodsBig.cod.trim()
            if (cod.isEmpty()) {
                return EventsGoods.Error("[SearchOnArtklDbfImpl.searchArtkl] Код для поиска не определен")
            }

            val result: MGoodsBig = mGoodsBig

            val artklName: CharField = dbArtkl.getField("NAME") as CharField

            // Поиск
            if (dbArtkl.find(cod)) {
                result.articul = artklName.get().trim()
                return EventsGoods.Success(data = result)
            }

            return EventsGoods.UnSuccess("Артикул по коду не найден")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[SearchOnArtklDbfImpl.searchArtkl] ${e.localizedMessage}")
        }
    }

    override suspend fun closeArtkl(artklBaseName: String) {
        dbInit = false
        if (this::dbArtkl.isInitialized) dbArtkl.close()
    }
}
