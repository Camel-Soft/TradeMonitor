package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showError
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import java.io.File
import java.io.IOException
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.camelsoft.trademonitor.BuildConfig

fun shareFile(context: Context, file: File, sign: String) {
    try {
        val pathURI = FileProvider.getUriForFile(getAppContext(),BuildConfig.APPLICATION_ID+".provider", file)
        val intentShare = Intent(Intent.ACTION_SEND)
        intentShare.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intentShare.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intentShare.putExtra(Intent.EXTRA_STREAM, pathURI)
        intentShare.putExtra(Intent.EXTRA_TEXT, sign)
        intentShare.type = "text/plain"
        startActivity(context, Intent.createChooser(intentShare, getAppContext().resources.getString(R.string.export)), null)
    }
    catch (e: IOException) {
        e.printStackTrace()
        showError(context, getAppContext().resources.getString(R.string.error_in)+" Files.shareFile: "+e.message) {}
    }
}
