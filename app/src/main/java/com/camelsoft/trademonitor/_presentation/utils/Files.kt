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
import com.camelsoft.trademonitor.common.events.EventsSync
import okhttp3.ResponseBody
import java.io.FileOutputStream

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

fun getFileFromBody(responseBody: ResponseBody?, fileFolder: File?, fileName: String): EventsSync<File> {
    try {
        if (responseBody == null) return EventsSync.Error("[Files.getFileFromBody] ${getAppContext().resources.getString(R.string.error_response_body)}")

        var folder = File(getAppContext().externalCacheDir, File.separator+"tmpFolder")
        if (fileFolder != null) folder = fileFolder

        if (!folder.exists()) {
            if (!folder.mkdirs()) return EventsSync.Error("[Files.getFileFromBody] ${getAppContext().resources.getString(R.string.error_folder_create)} - ${folder.absolutePath}")
        }

        var file = "tmpName.txt"
        if (fileName.isNotBlank()) file = fileName

        val fullName = folder.absolutePath.addSep()+file
        val fullFile = File(fullName)

        if (fullFile.exists()) {
            if (!fullFile.delete()) return EventsSync.Error("[Files.getFileFromBody] ${getAppContext().resources.getString(R.string.error_file_delete)} - ${fullFile.absolutePath}")
        }

        val inputStream = responseBody.byteStream()
        val fileOutputStream = FileOutputStream(fullName)
        val buffer = ByteArray(8 * 1024)
        var length: Int
        try {
            while (inputStream.read(buffer).also { length = it } != -1) {
                fileOutputStream.write(buffer, 0, length)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error("[Files.getFileFromBody] ${e.localizedMessage}")
        }
        finally {
            fileOutputStream.flush()
            fileOutputStream.close()
            inputStream.close()
        }

        return EventsSync.Success(fullFile)
    }
    catch (e: Exception) {
        e.printStackTrace()
        return EventsSync.Error("[Files.getFileFromBody] ${e.localizedMessage}")
    }
}
