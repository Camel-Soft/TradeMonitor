package com.camelsoft.trademonitor._presentation.utils.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.camelsoft.trademonitor.R

fun showConfirm(context: Context, title: String, message: String, click: () -> Unit) {
    AlertDialog.Builder(context)
        .setIcon(R.drawable.img_question_96)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(R.string.ok,
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                click()
            })
        .setNegativeButton(R.string.cancel,
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
        .create()
        .show()
}