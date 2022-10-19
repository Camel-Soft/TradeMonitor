package com.camelsoft.trademonitor._presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.camelsoft.trademonitor._presentation.models.MId

@SuppressLint("MissingPermission", "HardwareIds")
fun getTelephonyItems(appContext: Context): MId {
    try {
        // Q = 29, Android 10 in September 2019
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return MId(
                sdk = Build.VERSION.SDK_INT,
                id = Settings.System.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
            )
        }
        else {
            val telephonyManager = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return MId(
                sdk = Build.VERSION.SDK_INT,
                id = telephonyManager.getDeviceId(0)+"_"+Settings.System.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
            )
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        throw Exception ("[UtilsNet.getTelephonyItems] ${e.localizedMessage}")
    }
}

fun isOnline(appContext: Context): Boolean {
    try {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                // Сим-карта
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                // Wi-Fi
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                // ETHERNET
                return true
            }
        }
        return false
    }
    catch (e: Exception) {
        e.printStackTrace()
        throw Exception ("[UtilsNet.isOnline] ${e.localizedMessage}")
    }
}
