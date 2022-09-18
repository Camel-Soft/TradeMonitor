package com.camelsoft.trademonitor._presentation.dialogs.specify_ch_zn

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MChZnXmlHead
import com.camelsoft.trademonitor._presentation.utils.timeToChZn
import com.camelsoft.trademonitor.common.App

fun makeNoteChZn(head: MChZnXmlHead): String {
    var result = ""
    if (head.innMy.isNotEmpty()) {
        result += "${App.getAppContext().resources.getString(R.string.inn)}: ${head.innMy}\n"
    }
    if (head.dateDoc != 0L) {
        result += "${App.getAppContext().resources.getString(R.string.date)}: ${timeToChZn(head.dateDoc)}\n"
    }
    when (head.withdrawalType) {
        "PACKING" -> {
            result += "${App.getAppContext().resources.getString(R.string.withdrawal_full)}: ${
                App.getAppContext().resources.getString(
                    R.string.packing)}\n"
        }
        else -> {}
    }

    return result
}