package com.camelsoft.trademonitor._presentation.utils.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.camelsoft.trademonitor.R

fun showConfirm(context: Context, title: String, message: String, click: () -> Unit) {
    AlertDialog.Builder(context)
        .setIcon(R.drawable.img_question_48)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(R.string.ok) { dialog, id ->
            dialog.dismiss()
            click()
        }
        .setNegativeButton(R.string.cancel) { dialog, id ->
            dialog.dismiss()
        }
        .create()
        .show()
}