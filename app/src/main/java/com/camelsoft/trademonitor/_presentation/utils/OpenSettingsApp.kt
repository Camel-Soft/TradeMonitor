package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.utils.dialogs.ShowError

class OpenSettingsApp(private val context: Context) {

    init {
        openSettingsApp()
    }

    private fun openSettingsApp() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)

        }catch (e: Exception) {
            val errMessage = context.resources.getString(R.string.error_in)+
                    " OpenSettingsApp.openSettingsApp: ${e.message}"
            ShowError(context, errMessage) {}
        }
    }
}