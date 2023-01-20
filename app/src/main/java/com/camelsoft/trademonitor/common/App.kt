package com.camelsoft.trademonitor.common

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.Constants.Companion.OFFLINE_NOTIFICATION_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        createOfflineNotificationChannel()
    }

    init {
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null

        fun getAppContext() : Context {
            return appInstance!!.applicationContext
        }
    }

    private fun createOfflineNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelOffline = NotificationChannel(
                OFFLINE_NOTIFICATION_CHANNEL_ID,
                resources.getString(R.string.notification_name_offline),
                NotificationManager.IMPORTANCE_LOW  // Если выше, то со звуком
            )
            channelOffline.description = resources.getString(R.string.notification_description_offline)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelOffline)
        }
    }
}
