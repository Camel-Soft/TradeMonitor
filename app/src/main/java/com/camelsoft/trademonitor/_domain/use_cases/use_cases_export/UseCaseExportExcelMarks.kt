package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.MAlkoColl
import com.camelsoft.trademonitor._domain.libs.ExportExcelSheet
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File
import javax.inject.Inject

class UseCaseExportExcelMarks @Inject constructor(
    private val iRoom: IRoom,
    private val exportExcelSheet: ExportExcelSheet
) {
    suspend fun execute(alkoColl: MAlkoColl): EventsSync<File> {
        try {
            exportExcelSheet.open()

            // Заголовок документа
            exportExcelSheet.writeTitle(1,1, alkoColl.note)
            exportExcelSheet.writeText(1,2, App.getAppContext().resources.getString(R.string.coll_created)+": "+ timeToString(alkoColl.created))
            if (alkoColl.created != alkoColl.changed)
                exportExcelSheet.writeText(1,3, App.getAppContext().resources.getString(R.string.coll_changed)+": "+ timeToString(alkoColl.changed))
            exportExcelSheet.writeText(1,4, App.getAppContext().resources.getString(R.string.rec_total)+": "+alkoColl.total.toString())

            // Размер ячеек
            exportExcelSheet.setColumnSize(1, 30)
            exportExcelSheet.setColumnSize(2, 16)
            exportExcelSheet.setColumnSize(3, 15)
            exportExcelSheet.setColumnSize(4, 16)
            exportExcelSheet.setColumnSize(5, 12)
            exportExcelSheet.setColumnSize(6, 12)
            exportExcelSheet.setColumnSize(7, 30)
            exportExcelSheet.setColumnSize(8, 30)
            exportExcelSheet.setColumnSize(9, 12)

            // Header таблицы
            exportExcelSheet.writeHeader(1, 7, App.getAppContext().resources.getString(R.string.marka))
            exportExcelSheet.writeHeader(2, 7, App.getAppContext().resources.getString(R.string.marka_type))
            exportExcelSheet.writeHeader(3, 7, App.getAppContext().resources.getString(R.string.scancode))
            exportExcelSheet.writeHeader(4, 7, App.getAppContext().resources.getString(R.string.scancode_type))
            exportExcelSheet.writeHeader(5, 7, App.getAppContext().resources.getString(R.string.quantity))
            exportExcelSheet.writeHeader(6, 7, App.getAppContext().resources.getString(R.string.cena))
            exportExcelSheet.writeHeader(7, 7, App.getAppContext().resources.getString(R.string.name))
            exportExcelSheet.writeHeader(8, 7, App.getAppContext().resources.getString(R.string.note))
            exportExcelSheet.writeHeader(9, 7, App.getAppContext().resources.getString(R.string.status))

            // Заполение данными
            val list = iRoom.getRoomAlkoMarks(id_coll = alkoColl.id_coll)
            var i = 8
            list.forEach { alkoMark ->
                exportExcelSheet.writeCellStr(column = 1, row = i, alkoMark.marka)
                exportExcelSheet.writeCellStr(column = 2, row = i, getScanType(alkoMark.marka_type))
                exportExcelSheet.writeCellStr(column = 3, row = i, alkoMark.scancode)
                exportExcelSheet.writeCellStr(column = 4, row = i, getScanType(alkoMark.scancode_type))
                exportExcelSheet.writeCellNum(column = 5, row = i, toQuantity(alkoMark.quantity).toDouble())
                exportExcelSheet.writeCellNum(column = 6, row = i, toMoney(alkoMark.cena).toDouble())
                exportExcelSheet.writeCellStr(column = 7, row = i, alkoMark.name)
                exportExcelSheet.writeCellStr(column = 8, row = i, alkoMark.note)
                exportExcelSheet.writeCellStr(column = 9, row = i, getWrkMess(alkoMark.status_code).first+" "+ getErrMess(alkoMark.status_code).first)
                i++
            }

            exportExcelSheet.close()
            return EventsSync.Success(exportExcelSheet.getFile())

        }catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(App.getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExportExcelMarks.execute: "+e.message
            )
        }
    }
}