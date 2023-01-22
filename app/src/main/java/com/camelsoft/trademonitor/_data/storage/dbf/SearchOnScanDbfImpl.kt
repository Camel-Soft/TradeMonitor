package com.camelsoft.trademonitor._data.storage.dbf

import com.camelsoft.trademonitor._domain.api.offl_dbf.ISearchOnScan
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import com.camelsoft.trademonitor.common.settings.Settings
import org.xBaseJ.DBF
import org.xBaseJ.fields.CharField
import java.io.File

class SearchOnScanDbfImpl(private val settings: Settings): ISearchOnScan {
    private var dbInit = false
    private lateinit var dbScan: DBF

    override suspend fun openScan(scanBaseName: String, scanIndexName: String?): Boolean {
        try {
            val scanBaseFile = File(scanBaseName)
            if (!scanBaseFile.exists() || !scanBaseFile.isFile) {
                dbInit = false
                if (this::dbScan.isInitialized) dbScan.close()
                return false
            }
            if (scanIndexName == null) {
                dbInit = false
                if (this::dbScan.isInitialized) dbScan.close()
                return false
            }
            else {
                val scanIndexFile = File(scanIndexName)
                if (!scanIndexFile.exists() || !scanIndexFile.isFile) {
                    dbInit = false
                    if (this::dbScan.isInitialized) dbScan.close()
                    return false
                }
                dbScan = DBF(scanBaseName, "IBM866")
                dbScan.useIndex(scanIndexName)
                dbInit = true
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            dbInit = false
            if (this::dbScan.isInitialized) dbScan.close()
            return false
        }
    }

    override suspend fun searchScan(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (!dbInit) {
                return EventsGoods.Error("[SearchOnScanDbfImpl.searchScan] Подготовка баз данных для поиска не проведена")
            }

            val scancod: String = mGoodsBig.scancod.trim()
            if (scancod.isEmpty()) {
                return EventsGoods.Error("[SearchOnScanDbfImpl.searchScan] Сканкод для поиска не определен")
            }

            val result: MGoodsBig = mGoodsBig

            val searchScan1: String = scancod                                  // первый - без изменения
            val searchScan2: String = scancod.substring(0, scancod.length-1)   // второй - без контрольки
            val searchScan3: String = scancod.substring(1)            // третий - без нуля впереди
            val searchScan4: String = scancod.substring(1, scancod.length-1)   // четвертый - без нуля впереди и без контрольки
            var searchScan5: String? = null                                    // пятый - весовой товар
            if (settings.getPrefix().toInt() != 0 && scancod.substring(0,2) == settings.getPrefix() && scancod.length >= 12)
                searchScan5 = scancod.substring(0,7)
            var searchScan6: String? = null                                    // шестой - чужой весовой товар
            if (scancod.length >= 12)
                searchScan6 = scancod.substring(0,7)+"+++++"

            //val scanScancod: CharField = dbScan.getField("SCANCOD") as CharField
            val scanCod: CharField = dbScan.getField("COD") as CharField
            val scanName: CharField = dbScan.getField("NAME") as CharField
            val scanEdizm: CharField = dbScan.getField("EDIZM") as CharField

            // Первый проход
            if (dbScan.find(searchScan1)) {
                result.cod = scanCod.get()
                result.scancod_is_find = searchScan1
                result.name = scanName.get()
                result.ed_izm = scanEdizm.get()
                return EventsGoods.Success(data = result)
            }
            // Второй проход
            if (dbScan.find(searchScan2)) {
                result.cod = scanCod.get()
                result.scancod_is_find = searchScan2
                result.name = scanName.get()
                result.ed_izm = scanEdizm.get()
                return EventsGoods.Success(data = result)
            }
            // Третий проход
            if (dbScan.find(searchScan3)) {
                result.cod = scanCod.get()
                result.scancod_is_find = searchScan3
                result.name = scanName.get()
                result.ed_izm = scanEdizm.get()
                return EventsGoods.Success(data = result)
            }
            // Четвертый проход
            if (dbScan.find(searchScan4)) {
                result.cod = scanCod.get()
                result.scancod_is_find = searchScan4
                result.name = scanName.get()
                result.ed_izm = scanEdizm.get()
                return EventsGoods.Success(data = result)
            }
            // Пятый проход
            if (searchScan5 != null) {
                if (dbScan.find(searchScan5)) {
                    result.cod = scanCod.get()
                    result.scancod_is_find = searchScan5
                    result.name = scanName.get()
                    result.ed_izm = scanEdizm.get()
                    return EventsGoods.Success(data = result)
                }
            }
            // Шестой проход
            if (searchScan6 != null) {
                if (dbScan.find(searchScan6)) {
                    result.cod = scanCod.get()
                    result.scancod_is_find = searchScan6
                    result.name = scanName.get()
                    result.ed_izm = scanEdizm.get()
                    return EventsGoods.Success(data = result)
                }
            }

            return EventsGoods.UnSuccess("Товар по сканкоду не найден")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[SearchOnScanDbfImpl.searchScan] "+e.localizedMessage)
        }
    }

    override suspend fun closeScan(scanBaseName: String) {
        dbInit = false
        if (this::dbScan.isInitialized) dbScan.close()
    }
}
