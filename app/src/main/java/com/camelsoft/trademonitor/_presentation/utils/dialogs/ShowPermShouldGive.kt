package com.camelsoft.trademonitor._presentation.utils.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.openSettingsApp

fun showPermShouldGive(context: Context, click: () -> Unit) {
    AlertDialog.Builder(context)
        .setIcon(R.drawable.img_warning_48)
        .setTitle(R.string.need_permissions_title)
        .setMessage(R.string.need_permissions_message)
        .setCancelable(false)
        .setPositiveButton(R.string.settings_app) { dialog, id ->
            dialog.dismiss()
            openSettingsApp(context)
            click()
        }
        .setNegativeButton(R.string.exit) { dialog, id ->
            dialog.dismiss()
            click()
        }
        .create()
        .show()
}