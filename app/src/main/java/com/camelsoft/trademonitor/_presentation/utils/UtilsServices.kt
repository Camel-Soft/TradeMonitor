package com.camelsoft.trademonitor._presentation.utils

import android.app.ActivityManager
import android.content.Context

@Suppress("DEPRECATION")
fun <T> isServiceRunning(context: Context, service: Class<T>) =
    (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }
