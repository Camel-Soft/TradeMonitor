package com.camelsoft.trademonitor._domain.libs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.camelsoft.trademonitor._domain.api.ITelephony
import com.camelsoft.trademonitor._presentation.models.MId
import com.camelsoft.trademonitor.common.App

class TelephonyImpl: ITelephony {
    @SuppressLint("MissingPermission", "HardwareIds")
    override suspend fun getTelephonyItems(): MId {
        try {
            // Q = 29, Android 10 in September 2019
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return MId(
                    sdk = Build.VERSION.SDK_INT,
                    id = "",
                    aid = Settings.System.getString(App.getAppContext().contentResolver, Settings.Secure.ANDROID_ID)
                )
            }
            else {
                try {
                    val telephonyManager = App.getAppContext()
                        .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    return MId(
                        sdk = Build.VERSION.SDK_INT,
                        id = telephonyManager.getDeviceId(0),
                        aid = Settings.System.getString(App.getAppContext().contentResolver, Settings.Secure.ANDROID_ID)
                    )
                }
                catch (_: Exception) {
                    return MId(
                        sdk = Build.VERSION.SDK_INT,
                        id = "",
                        aid = Settings.System.getString(App.getAppContext().contentResolver, Settings.Secure.ANDROID_ID)
                    )
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception ("[TelephonyImpl.getTelephonyItems] ${e.localizedMessage}")
        }
    }
}
