package com.camelsoft.trademonitor._presentation.utils.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.camelsoft.trademonitor.R

fun showError(context: Context, message: String, click: () -> Unit) {
    AlertDialog.Builder(context)
        .setIcon(R.drawable.img_warning_96)
        .setTitle(R.string.error)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(R.string.ok,
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                click()
            })
        .create()
        .show()
}