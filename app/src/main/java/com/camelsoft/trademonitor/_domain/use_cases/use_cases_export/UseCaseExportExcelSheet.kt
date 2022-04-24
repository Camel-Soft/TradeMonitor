package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.utils.ExcelWriteSheet
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File
import javax.inject.Inject

class UseCaseExportExcelSheet @Inject constructor(
    private val iRoom: IRoom,
    private val excel: ExcelWriteSheet
) {
    suspend fun execute(priceColl: MPriceColl): EventsSync<File> {
        try {
            excel.open()

            // Заголовок документа
            excel.writeTitle(1,1, priceColl.note)
            excel.writeText(1,2, getAppContext().resources.getString(R.string.coll_created)+": "+ timeToString(priceColl.created))
            if (priceColl.created != priceColl.changed)
                excel.writeText(1,3, getAppContext().resources.getString(R.string.coll_changed)+": "+timeToString(priceColl.changed))
            excel.writeText(1,4, getAppContext().resources.getString(R.string.rec_total)+": "+priceColl.total.toString())

            // Размер ячеек
            excel.setColumnSize(1, 15)
            excel.setColumnSize(2, 16)
            excel.setColumnSize(3, 12)
            excel.setColumnSize(4, 12)
            excel.setColumnSize(5, 12)
            excel.setColumnSize(6, 30)
            excel.setColumnSize(7, 30)
            excel.setColumnSize(8, 12)

            // Header таблицы
            excel.writeHeader(1, 7, getAppContext().resources.getString(R.string.scancode))
            excel.writeHeader(2, 7, getAppContext().resources.getString(R.string.scancode_type))
            excel.writeHeader(3, 7, getAppContext().resources.getString(R.string.quantity))
            excel.writeHeader(4, 7, getAppContext().resources.getString(R.string.ed_izm))
            excel.writeHeader(5, 7, getAppContext().resources.getString(R.string.cena))
            excel.writeHeader(6, 7, getAppContext().resources.getString(R.string.name))
            excel.writeHeader(7, 7, getAppContext().resources.getString(R.string.note))
            excel.writeHeader(8, 7, getAppContext().resources.getString(R.string.status))

            // Заполение данными
            val list = iRoom.getRoomGoodes(id_coll = priceColl.id_coll)
            var i = 8
            list.forEach { priceGoods ->
                excel.writeCellStr(column = 1, row = i, priceGoods.scancode)
                excel.writeCellStr(column = 2, row = i, getScanType(priceGoods.scancode_type))
                excel.writeCellNum(column = 3, row = i, toQuantity(priceGoods.quantity).toDouble())
                excel.writeCellStr(column = 4, row = i, priceGoods.ed_izm)
                excel.writeCellNum(column = 5, row = i, toMoney(priceGoods.cena).toDouble())
                excel.writeCellStr(column = 6, row = i, priceGoods.name)
                excel.writeCellStr(column = 7, row = i, priceGoods.note)
                excel.writeCellStr(column = 8, row = i, getWrkMess(priceGoods.status_code).first+" "+getErrMess(priceGoods.status_code).first)
                i++
            }

            excel.close()
            return EventsSync.Success(excel.getFile())

        }catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(
                getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExportExcelSheet.execute: "+e.message
            )
        }
    }
}