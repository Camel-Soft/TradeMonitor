package com.camelsoft.trademonitor._presentation.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.api.repo.IOfflBase
import com.camelsoft.trademonitor._presentation.models.MOffline
import com.camelsoft.trademonitor._presentation.utils.timeToLog
import com.camelsoft.trademonitor.common.Constants.Companion.ACTION_BROADCAST_OFFLINE
import com.camelsoft.trademonitor.common.settings.Settings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class OfflineService : LifecycleService() {
    @Inject lateinit var iOfflBase: IOfflBase
    @Inject lateinit var settings: Settings
    private var mOffline = MOffline(
        isRunning = false,
        info = "",
        stageCurrent = 0,
        stageTotal = 0,
        stageName = "",
        stagePercent = 0
    )

    override fun onCreate() {
        super.onCreate()
        mOffline = MOffline(
            isRunning = true,
            info = "${timeToLog()} ${resources.getString(R.string.start)} \n",
            stageCurrent = 0,
            stageTotal = 3,
            stageName = "",
            stagePercent = 0
        )
        settings.putMOffline(mOffline)
        sendIntent(mOffline)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                intent?.let { Log.d("srv", "onStartCommand: Intent action: ${ it.action}") }
                Log.d("srv", "onStartCommand: flags: $flags")
                Log.d("srv", "onStartCommand: startId: $startId")

                //sendBroadcast(Intent("sdf"))

                var i = 7
                while (i>-2) {
                    Log.d("srv", "Бесконечный цикл - flags: $i - startId: $startId")
                    //settings.putTest("Бесконечный цикл - flags: $i - startId: $startId")
                    delay(1000)
                    i--
                    var ggg = 5/i
                }
                stopSelf()
            }
            catch (e: Exception) {
                e.printStackTrace()
                Log.e("srv", "exception: ${e.localizedMessage}")
            }
        }

        return START_NOT_STICKY


    }

    override fun onDestroy() {
        Log.d("srv", "onDestroy")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("srv", "onTaskRemoved")
        super.onTaskRemoved(rootIntent)
    }


    private fun sendIntent(mOffline: MOffline) {
        val intent = Intent(ACTION_BROADCAST_OFFLINE)
        intent.putExtra("mOffline", mOffline)
        sendBroadcast(intent)
    }
}
