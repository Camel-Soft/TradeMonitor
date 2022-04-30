package com.camelsoft.trademonitor._domain.utils

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceGoods
import com.camelsoft.trademonitor._presentation.utils.timeToString
import com.camelsoft.trademonitor._presentation.utils.toMoney
import com.camelsoft.trademonitor._presentation.utils.toQuantity
import com.camelsoft.trademonitor.common.App
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

class ExportJsonGoodes {
    private var fileName = "ExportJSON.json"
    private lateinit var file: File
    private lateinit var fileWriter: FileWriter
    private lateinit var arrayGoodes: JSONArray

    fun setFileName(newFileName: String) { fileName = newFileName }

    fun open() {
        try {
            file = File(App.getAppContext().externalCacheDir, File.separator+fileName)
            file.delete()
            if (file.exists()) throw Exception(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " ExportJsonGoodes.open: "+ App.getAppContext().resources.getString(R.string.error_clear_file)
            )
            if (!file.createNewFile()) throw Exception(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " ExportJsonGoodes.open: "+ App.getAppContext().resources.getString(R.string.error_create_file)
            )

            arrayGoodes = JSONArray()
            fileWriter = FileWriter(file)
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonGoodes.open: "+e.message)
        }
    }

    fun addToArrayGoodes(objGoods: JSONObject) {
        try {
            arrayGoodes.put(objGoods)
        }
        catch (e: JSONException) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonGoodes.addToArrayGoodes: "+e.message)
        }
    }

    fun createGoods(priceGoods: MPriceGoods): JSONObject {
        try {
            val objGoods = JSONObject()
            objGoods.put("scancode", priceGoods.scancode)
            objGoods.put("scancode_type", priceGoods.scancode_type)
            objGoods.put("quantity", toQuantity(priceGoods.quantity))
            objGoods.put("ed_izm", priceGoods.ed_izm)
            objGoods.put("cena", toMoney(priceGoods.cena))
            objGoods.put("name", priceGoods.name)
            objGoods.put("note", priceGoods.note)
            objGoods.put("status_code", priceGoods.status_code.toString())
            return objGoods
        }
        catch (e: JSONException) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonGoodes.createGoods: "+e.message)
        }
    }

    fun createFinalJsonGoods(priceColl: MPriceColl) {
        try {
            val finalJson = JSONObject()
            finalJson.put("coll", priceColl.note)
            finalJson.put("created", timeToString(priceColl.created))
            finalJson.put("changed", timeToString(priceColl.changed))
            finalJson.put("total", priceColl.total.toString())
            finalJson.put("goodes", arrayGoodes)
            fileWriter.write(finalJson.toString(1))
        }
        catch (e: JSONException) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonGoodes.createFinalJsonGoods: "+e.message)
        }
    }

    fun close() {
        try {
            fileWriter.flush()
            fileWriter.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(App.getAppContext().resources.getString(R.string.error_in)+" ExportJsonGoodes.close: "+e.message)
        }
    }

    fun getFile(): File { return file }
}