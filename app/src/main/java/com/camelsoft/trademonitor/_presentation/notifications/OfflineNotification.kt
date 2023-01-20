package com.camelsoft.trademonitor._presentation.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.activity_main.ActivityMain
import com.camelsoft.trademonitor._presentation.models.MOffline
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.Constants.Companion.NAVIGATE_FRAGMENT_KEY
import com.camelsoft.trademonitor.common.Constants.Companion.NAVIGATE_FRAGMENT_VALUE_OFFLINE
import com.camelsoft.trademonitor.common.Constants.Companion.OFFLINE_NOTIFICATION_CHANNEL_ID
import com.camelsoft.trademonitor.common.Constants.Companion.OFFLINE_NOTIFICATION_ID
import com.camelsoft.trademonitor.common.Constants.Companion.OFFLINE_PENDING_INTENT_REQUEST_CODE

class OfflineNotification {
    private val appContext = getAppContext()
    private val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var notificationOffline: Notification? = null
    private val activityIntent = Intent(appContext, ActivityMain::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra(NAVIGATE_FRAGMENT_KEY, NAVIGATE_FRAGMENT_VALUE_OFFLINE)
    }
    private val activityPendingIntent = PendingIntent.getActivity(
        appContext,
        OFFLINE_PENDING_INTENT_REQUEST_CODE,
        activityIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    fun show(mOffline: MOffline) {
        if (mOffline.status < 1) {
            notificationOffline?.let { notificationManager.cancel(OFFLINE_NOTIFICATION_ID) }
            return
        }

        notificationOffline = NotificationCompat.Builder(appContext, OFFLINE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.img_download_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentTitle("${mOffline.stageName}  ${mOffline.stageCurrent}/${mOffline.stageTotal}")
            .setContentText("${mOffline.stagePercent}%")
            .setProgress(100, mOffline.stagePercent, false)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(OFFLINE_NOTIFICATION_ID, notificationOffline)
    }
}

//        activityPendingIntent = TaskStackBuilder.create(appContext).run {
//            addNextIntentWithParentStack(activityIntent)
//            getPendingIntent(OFFLINE_PENDING_INTENT_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
//        }

//            .setStyle(NotificationCompat.BigTextStyle()
//                .setBigContentTitle("${mOffline.stageName}  ${mOffline.stageCurrent}/${mOffline.stageTotal}")
//                .bigText(mOffline.info)
//            )
