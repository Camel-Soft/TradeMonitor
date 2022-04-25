package com.camelsoft.trademonitor._domain.utils

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.toQuantity
import com.camelsoft.trademonitor.common.App
import java.io.File
import java.io.FileWriter

class SouthRevision {
    private var fileName = "OUT720L.TXT"
    private lateinit var file: File
    private lateinit var fileWriter: FileWriter

    fun setFileName(newFileName: String) { fileName = newFileName }

    fun open() {
        try {
            file = File(App.getAppContext().externalCacheDir, File.separator+fileName)
            file.delete()
            if (file.exists()) throw Exception(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " SouthRevision.open: "+ App.getAppContext().resources.getString(R.string.error_clear_file)
            )
            if (!file.createNewFile()) throw Exception(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " SouthRevision.open: "+ App.getAppContext().resources.getString(R.string.error_create_file)
            )

            fileWriter = FileWriter(file)
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" SouthRevision.open: "+e.message)
        }
    }

    fun add (scancode: String, quantity: Float) {
        try {
            var newScan = scancode.trim()
            for (i in newScan.length..20) newScan += " "
            val newQuan = toQuantity(quantity)
            fileWriter.write(newScan+newQuan+"\r\n")
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" SouthRevision.add: "+e.message)
        }
    }

    fun close() {
        try {
            fileWriter.flush()
            fileWriter.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" SouthRevision.close: "+e.message)
        }
    }

    fun getFile(): File { return file }
}