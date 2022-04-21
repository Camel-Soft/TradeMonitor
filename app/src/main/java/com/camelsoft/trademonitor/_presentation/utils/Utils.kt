package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App

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
            val wrkMesText = App.getAppContext().resources.getString(R.string.inserted)
            val wrkMesColor = R.color.green_300
            return Pair(wrkMesText, wrkMesColor)
        }
        2 -> {
            val wrkMesText = App.getAppContext().resources.getString(R.string.updated)
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
            val wrkMesText = App.getAppContext().resources.getString(R.string.error_scancode)
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

fun hideKeyboard(context: Context, view: View?) {
    view?.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}