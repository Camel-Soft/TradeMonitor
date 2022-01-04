package com.camelsoft.trademonitor._presentation.utils

import android.Manifest
import android.content.Context
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.resource.ResSync
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.lang.Exception

class Permissions(
    private val context: Context,
    private val report: (ResSync<Boolean>) -> Unit) {

    init {
        getPermissions()
    }

    private fun getPermissions() {
        try {
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(permReport: MultiplePermissionsReport?) {
                        permReport?.let {
                            if(permReport.areAllPermissionsGranted())
                                report(ResSync.Success<Boolean>(true))
                            else
                                report(ResSync.Success<Boolean>(false))
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) { token?.continuePermissionRequest() }
                })
                .withErrorListener {
                    val errMessage = context.resources.getString(R.string.error_in)+
                            " Permissions.DexterError: ${it.name}"
                    report(ResSync.Error<Boolean>(errMessage))
                }
                .check()

        }catch (e: Exception) {
            e.printStackTrace()
            val errMessage = context.resources.getString(R.string.error_in)+
                    " Permissions.getPermissions: ${e.message}"
            report(ResSync.Error<Boolean>(errMessage))
        }
    }
}