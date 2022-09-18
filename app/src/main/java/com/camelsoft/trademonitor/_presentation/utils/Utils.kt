package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.startActivity
import com.camelsoft.trademonitor.BuildConfig
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getWrkCode(code: Int): Int {
    return code / 100
}

fun getErrCode(code: Int): Int {
    return code % 100
}

fun <T> genColorIdFromList(list: List<T>): String {
    return if (list.isEmpty()) "0"
    else (list.size % 10).toString()
}

fun getWrkMess(code: Int): Pair<String, Int> {
    when (getWrkCode(code)) {
        1 -> {
            val wrkMesText = getAppContext().resources.getString(R.string.inserted)
            val wrkMesColor = R.color.green_300
            return Pair(wrkMesText, wrkMesColor)
        }
        2 -> {
            val wrkMesText = getAppContext().resources.getString(R.string.updated)
            val wrkMesColor = R.color.blue_300
            return Pair(wrkMesText, wrkMesColor)
        }
        else -> {
            val wrkMesText = ""
            val wrkMesColor = R.color.black
            return Pair(wrkMesText, wrkMesColor)
        }
    }
}

fun getErrMess(code: Int): Pair<String, Int> {
    return when (getErrCode(code)) {
        1 -> {
            val wrkMesText = getAppContext().resources.getString(R.string.error_scancode)
            val wrkMesColor = R.color.red_100
            Pair(wrkMesText, wrkMesColor)
        }
        else -> {
            val wrkMesText = ""
            val wrkMesColor = R.color.black
            Pair(wrkMesText, wrkMesColor)
        }
    }
}

fun getHolderColor(codeColor: String): Int {
    return when (codeColor) {
        "0" -> R.color.rv_goods_0
        "1" -> R.color.rv_goods_1
        "2" -> R.color.rv_goods_2
        "3" -> R.color.rv_goods_3
        "4" -> R.color.rv_goods_4
        "5" -> R.color.rv_goods_5
        "6" -> R.color.rv_goods_6
        "7" -> R.color.rv_goods_7
        "8" -> R.color.rv_goods_8
        "9" -> R.color.rv_goods_9
        else -> R.color.white
    }
}

fun che(): Boolean {
    val file = File(getAppContext().externalCacheDir, File.separator+"app.log")
    if (file.exists()) return false
    val expired1 = SimpleDateFormat("yyyy-MM-dd").parse("2022-06-28")
    val expired2 = SimpleDateFormat("yyyy-MM-dd").parse("2022-07-29")
    val today = Date()
    return if ((today.time > expired1.time) && (today.time < expired2.time)) true
    else {
        file.createNewFile()
        false
    }
}

fun hideKeyboard(context: Context, view: View?) {
    view?.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun writeDeveloper(context: Context) {
    val intentEmail = Intent(Intent.ACTION_SENDTO)
    intentEmail.data = Uri.parse("mailto:")
    intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf(getAppContext().resources.getString(R.string.developer_email)))
    intentEmail.putExtra(Intent.EXTRA_SUBJECT, getAppContext().resources.getString(R.string.developer_request))
    intentEmail.putExtra(Intent.EXTRA_TEXT, getAppContext().resources.getString(R.string.version)+" "+ BuildConfig.VERSION_NAME)
    startActivity(context, Intent.createChooser(intentEmail, getAppContext().resources.getString(R.string.developer_write)), null)
}

fun String.rm001d() = this.replace(oldValue = "\u001d", newValue = "", ignoreCase = false)

fun String.trim001d(): String {
    if (this.isEmpty()) return this
    var result = this
    if (result.substring(0,1) == "\u001d") result = result.substring(1)
    if (result.substring(result.count()-1) == "\u001d") result = result.substring(0, result.count()-1)
    return result
}
