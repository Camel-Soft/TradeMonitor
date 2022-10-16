package com.camelsoft.trademonitor._domain.libs

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import jxl.Workbook
import jxl.format.Colour
import jxl.format.UnderlineStyle
import jxl.write.*
import jxl.write.Number
import java.io.File

class ExportExcelSheet {
    private var fileName = "ExportExcel.xls"
    private lateinit var file: File
    private lateinit var workbook: WritableWorkbook
    private lateinit var sheet1: WritableSheet
    private lateinit var formatTitle: WritableCellFormat
    private lateinit var formatHeader: WritableCellFormat
    private lateinit var formatCell: WritableCellFormat
    private lateinit var formatText: WritableCellFormat

    fun setFileName(newFileName: String) { fileName = newFileName }

    fun open() {
        try {
            file = File(getAppContext().externalCacheDir, File.separator+fileName)
            file.delete()
            if (file.exists()) throw Exception(
                getAppContext().resources.getString(R.string.error_in)+
                        " ExportExcelSheet.open: "+getAppContext().resources.getString(R.string.error_clear_file)
            )
            workbook = Workbook.createWorkbook(file)
            sheet1 = workbook.createSheet(getAppContext().resources.getString(R.string.sheet_1), 0)

            formatTitle = WritableCellFormat()
            formatTitle.setFont(WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false, UnderlineStyle.SINGLE, Colour.BLACK))
            formatTitle.alignment = jxl.format.Alignment.LEFT
            formatTitle.verticalAlignment = jxl.format.VerticalAlignment.CENTRE
            formatTitle.wrap = false

            formatHeader = WritableCellFormat()
            formatHeader.setFont(WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK))
            formatHeader.alignment = jxl.format.Alignment.CENTRE
            formatHeader.verticalAlignment = jxl.format.VerticalAlignment.CENTRE
            formatHeader.setBorder(jxl.format.Border.TOP,jxl.format.BorderLineStyle.MEDIUM, Colour.BLACK)
            formatHeader.setBorder(jxl.format.Border.BOTTOM,jxl.format.BorderLineStyle.MEDIUM, Colour.BLACK)
            formatHeader.setBorder(jxl.format.Border.LEFT,jxl.format.BorderLineStyle.MEDIUM, Colour.BLACK)
            formatHeader.setBorder(jxl.format.Border.RIGHT,jxl.format.BorderLineStyle.MEDIUM, Colour.BLACK)
            formatHeader.wrap = true

            formatCell = WritableCellFormat()
            formatCell.setFont(WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK))
            formatCell.alignment = jxl.format.Alignment.LEFT
            formatCell.verticalAlignment = jxl.format.VerticalAlignment.CENTRE
            formatCell.setBorder(jxl.format.Border.TOP,jxl.format.BorderLineStyle.THIN, Colour.BLACK)
            formatCell.setBorder(jxl.format.Border.BOTTOM,jxl.format.BorderLineStyle.THIN, Colour.BLACK)
            formatCell.setBorder(jxl.format.Border.LEFT,jxl.format.BorderLineStyle.THIN, Colour.BLACK)
            formatCell.setBorder(jxl.format.Border.RIGHT,jxl.format.BorderLineStyle.THIN, Colour.BLACK)
            formatCell.wrap = true

            formatText = WritableCellFormat()
            formatText.setFont(WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK))
            formatText.alignment = jxl.format.Alignment.LEFT
            formatText.verticalAlignment = jxl.format.VerticalAlignment.CENTRE
            formatText.wrap = false
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(getAppContext().resources.getString(R.string.error_in)+" ExportExcelSheet.open: "+e.message)
        }
    }

    fun writeTitle(column: Int, row: Int, cont: String) {
        sheet1.addCell(Label(column, row, cont, formatTitle))
    }

    fun writeHeader(column: Int, row: Int, cont: String) {
        sheet1.addCell(Label(column, row, cont, formatHeader))
    }

    fun writeCellStr(column: Int, row: Int, cont: String) {
        sheet1.addCell(Label(column, row, cont, formatCell))
    }

    fun writeCellNum(column: Int, row: Int, cont: Double) {
        sheet1.addCell(Number(column, row, cont, formatCell))
    }

    fun writeText(column: Int, row: Int, cont: String) {
        sheet1.addCell(Label(column, row, cont, formatText))
    }

    fun setColumnSize(column: Int, sizeInChars: Int) {
        sheet1.setColumnView(column, sizeInChars)
    }

    fun close() {
        try {
            workbook.write()
            workbook.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(getAppContext().resources.getString(R.string.error_in)+" ExportExcelSheet.close: "+e.message)
        }
    }

    fun getFile(): File { return file }
}