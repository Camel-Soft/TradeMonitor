package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Headers
import java.util.*

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

fun Headers.isStatusExists(): Boolean {
    this.forEach { if (it.first.lowercase().trim() == "status") return true }
    return false
}

fun Headers.isContentTypeExists(): Boolean {
    this.forEach { if (it.first.lowercase().trim() == "content-type") return true }
    return false
}

fun Headers.isContentLengthExists(): Boolean {
    this.forEach { if (it.first.lowercase().trim() == "content-length") return true }
    return false
}
