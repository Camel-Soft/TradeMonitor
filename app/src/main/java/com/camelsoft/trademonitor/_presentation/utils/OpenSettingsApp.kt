package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showError

fun openSettingsApp(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)

    }catch (e: Exception) {
        e.printStackTrace()
        val errMessage = context.resources.getString(R.string.error_in)+
                " OpenSettingsApp.openSettingsApp: ${e.message}"
        showError(context, errMessage) {}
    }
}