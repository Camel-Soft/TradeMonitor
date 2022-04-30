package com.camelsoft.trademonitor._domain.utils

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MAlkoColl
import com.camelsoft.trademonitor._domain.models.MAlkoMark
import com.camelsoft.trademonitor._presentation.utils.timeToString
import com.camelsoft.trademonitor._presentation.utils.toMoney
import com.camelsoft.trademonitor._presentation.utils.toQuantity
import com.camelsoft.trademonitor.common.App
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

class ExportJsonMarks {
    private var fileName = "ExportJSON.json"
    private lateinit var file: File
    private lateinit var fileWriter: FileWriter
    private lateinit var arrayMarks: JSONArray

    fun setFileName(newFileName: String) { fileName = newFileName }

    fun open() {
        try {
            file = File(App.getAppContext().externalCacheDir, File.separator+fileName)
            file.delete()
            if (file.exists()) throw Exception(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " ExportJsonMarks.open: "+ App.getAppContext().resources.getString(R.string.error_clear_file)
            )
            if (!file.createNewFile()) throw Exception(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " ExportJsonMarks.open: "+ App.getAppContext().resources.getString(R.string.error_create_file)
            )

            arrayMarks = JSONArray()
            fileWriter = FileWriter(file)
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonMarks.open: "+e.message)
        }
    }

    fun addToArrayMarks(objMark: JSONObject) {
        try {
            arrayMarks.put(objMark)
        }
        catch (e: JSONException) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonMarks.addToArrayMarks: "+e.message)
        }
    }

    fun createMark(alkoMark: MAlkoMark): JSONObject {
        try {
            val objMark = JSONObject()
            objMark.put("marka", alkoMark.marka)
            objMark.put("marka_type", alkoMark.marka_type)
            objMark.put("scancode", alkoMark.scancode)
            objMark.put("scancode_type", alkoMark.scancode_type)
            objMark.put("quantity", toQuantity(alkoMark.quantity))
            objMark.put("cena", toMoney(alkoMark.cena))
            objMark.put("name", alkoMark.name)
            objMark.put("note", alkoMark.note)
            objMark.put("status_code", alkoMark.status_code.toString())
            return objMark
        }
        catch (e: JSONException) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonMarks.createMark: "+e.message)
        }
    }

    fun createFinalJsonMark(alkoColl: MAlkoColl) {
        try {
            val finalJson = JSONObject()
            finalJson.put("coll", alkoColl.note)
            finalJson.put("created", timeToString(alkoColl.created))
            finalJson.put("changed", timeToString(alkoColl.changed))
            finalJson.put("total", alkoColl.total.toString())
            finalJson.put("marks", arrayMarks)
            fileWriter.write(finalJson.toString(1))
        }
        catch (e: JSONException) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonMarks.createFinalJsonMark: "+e.message)
        }
    }

    fun close() {
        try {
            fileWriter.flush()
            fileWriter.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonMarks.close: "+e.message)
        }
    }

    fun getFile(): File { return file }
}