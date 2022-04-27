package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.utils.ExportExcelSheet
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File
import javax.inject.Inject

class UseCaseExportExcelSheet @Inject constructor(
    private val iRoom: IRoom,
    private val exportExcelSheet: ExportExcelSheet
) {
    suspend fun execute(priceColl: MPriceColl): EventsSync<File> {
        try {
            exportExcelSheet.open()

            // Заголовок документа
            exportExcelSheet.writeTitle(1,1, priceColl.note)
            exportExcelSheet.writeText(1,2, getAppContext().resources.getString(R.string.coll_created)+": "+ timeToString(priceColl.created))
            if (priceColl.created != priceColl.changed)
                exportExcelSheet.writeText(1,3, getAppContext().resources.getString(R.string.coll_changed)+": "+timeToString(priceColl.changed))
            exportExcelSheet.writeText(1,4, getAppContext().resources.getString(R.string.rec_total)+": "+priceColl.total.toString())

            // Размер ячеек
            exportExcelSheet.setColumnSize(1, 15)
            exportExcelSheet.setColumnSize(2, 16)
            exportExcelSheet.setColumnSize(3, 12)
            exportExcelSheet.setColumnSize(4, 12)
            exportExcelSheet.setColumnSize(5, 12)
            exportExcelSheet.setColumnSize(6, 30)
            exportExcelSheet.setColumnSize(7, 30)
            exportExcelSheet.setColumnSize(8, 12)

            // Header таблицы
            exportExcelSheet.writeHeader(1, 7, getAppContext().resources.getString(R.string.scancode))
            exportExcelSheet.writeHeader(2, 7, getAppContext().resources.getString(R.string.scancode_type))
            exportExcelSheet.writeHeader(3, 7, getAppContext().resources.getString(R.string.quantity))
            exportExcelSheet.writeHeader(4, 7, getAppContext().resources.getString(R.string.ed_izm))
            exportExcelSheet.writeHeader(5, 7, getAppContext().resources.getString(R.string.cena))
            exportExcelSheet.writeHeader(6, 7, getAppContext().resources.getString(R.string.name))
            exportExcelSheet.writeHeader(7, 7, getAppContext().resources.getString(R.string.note))
            exportExcelSheet.writeHeader(8, 7, getAppContext().resources.getString(R.string.status))

            // Заполение данными
            val list = iRoom.getRoomGoodes(id_coll = priceColl.id_coll)
            var i = 8
            list.forEach { priceGoods ->
                exportExcelSheet.writeCellStr(column = 1, row = i, priceGoods.scancode)
                exportExcelSheet.writeCellStr(column = 2, row = i, getScanType(priceGoods.scancode_type))
                exportExcelSheet.writeCellNum(column = 3, row = i, toQuantity(priceGoods.quantity).toDouble())
                exportExcelSheet.writeCellStr(column = 4, row = i, priceGoods.ed_izm)
                exportExcelSheet.writeCellNum(column = 5, row = i, toMoney(priceGoods.cena).toDouble())
                exportExcelSheet.writeCellStr(column = 6, row = i, priceGoods.name)
                exportExcelSheet.writeCellStr(column = 7, row = i, priceGoods.note)
                exportExcelSheet.writeCellStr(column = 8, row = i, getWrkMess(priceGoods.status_code).first+" "+getErrMess(priceGoods.status_code).first)
                i++
            }

            exportExcelSheet.close()
            return EventsSync.Success(exportExcelSheet.getFile())

        }catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(
                getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExportExcelSheet.execute: "+e.message
            )
        }
    }
}