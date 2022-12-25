package com.camelsoft.trademonitor._presentation.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.camelsoft.trademonitor._presentation.api.repo.IOfflBase
import kotlinx.coroutines.*
import javax.inject.Inject

class OfflineService : LifecycleService() {
    @Inject lateinit var iOfflBase: IOfflBase
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        Log.d("srv", "onCreate*******************")
    }

    override fun onDestroy() {
        Log.d("srv", "onDestroy")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch(Dispatchers.IO) {
            intent?.let { Log.d("srv", "onStartCommand: Intent action: ${ it.action}") }
            Log.d("srv", "onStartCommand: flags: $flags")
            Log.d("srv", "onStartCommand: startId: $startId")

            sendBroadcast(Intent("sdf"))

            if (!isRunning) {
                isRunning = true
                var i = 7
                while (i>0) {
                    Log.d("srv", "Бесконечный цикл - flags: $flags - startId: $startId")
                    delay(1000)
                    i--
                }
                isRunning = false
                stopSelf()
            }
            else
                Log.e("srv", "I am RUNNING NOW")

        }

        return START_NOT_STICKY
    }


}
