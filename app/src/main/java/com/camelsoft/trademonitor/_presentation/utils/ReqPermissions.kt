package com.camelsoft.trademonitor._presentation.utils

import android.Manifest
import android.content.Context
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.events.EventsSync
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.lang.Exception

fun reqPermissions(context: Context, report: (EventsSync<Boolean>) -> Unit) {
    try {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            )
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(permReport: MultiplePermissionsReport?) {
                    permReport?.let {
                        if(permReport.areAllPermissionsGranted())
                            report(EventsSync.Success<Boolean>(true))
                        else
                            report(EventsSync.Success<Boolean>(false))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) { token?.continuePermissionRequest() }
            })
            .withErrorListener {
                val errMessage = context.resources.getString(R.string.error_in)+
                        " ReqPermissions.DexterError: ${it.name}"
                report(EventsSync.Error<Boolean>(errMessage))
            }
            .check()

    }catch (e: Exception) {
        e.printStackTrace()
        val errMessage = context.resources.getString(R.string.error_in)+
                " ReqPermissions.getPermissions: ${e.message}"
        report(EventsSync.Error<Boolean>(errMessage))
    }
}