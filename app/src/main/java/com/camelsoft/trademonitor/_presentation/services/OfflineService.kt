package com.camelsoft.trademonitor._presentation.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.api.repo.IOfflBase
import com.camelsoft.trademonitor._presentation.models.MOffline
import com.camelsoft.trademonitor._presentation.utils.timeToLog
import com.camelsoft.trademonitor.common.Constants.Companion.ACTION_BROADCAST_OFFLINE
import com.camelsoft.trademonitor.common.events.EventsProgress
import com.camelsoft.trademonitor.common.settings.Settings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class OfflineService : LifecycleService() {
    @Inject lateinit var iOfflBase: IOfflBase
    @Inject lateinit var settings: Settings
    private lateinit var sourceZip: File
    private var mOffline = MOffline(
        status = 0,
        info = "",
        stageCurrent = 0,
        stageTotal = 0,
        stageName = "",
        stagePercent = 0
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                if (mOffline.status != 0) return@launch

                // Стадия 1 - Копирование
                mOffline = MOffline(
                    status = 1,
                    info = "${timeToLog()} ${resources.getString(R.string.stage_copy)}\n",
                    stageCurrent = 1,
                    stageTotal = 3,
                    stageName = resources.getString(R.string.stage_copy),
                    stagePercent = 0
                )
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                iOfflBase.getOfflBase().collect {
                    when (it) {
                        is EventsProgress.Success -> {
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            sourceZip = it.data
                        }
                        is EventsProgress.UnSuccess -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()} ${resources.getString(R.string.info)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Error -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()} ${resources.getString(R.string.error)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Progress -> {
                            mOffline.stageName = it.stage
                            mOffline.stagePercent = it.percent
                            sendIntent(mOffline)
                        }
                    }
                }

                // Стадия 2 - Распаковка
                Log.d("srv", "Я тут .....   стадия 2")


//                if (!this@OfflineService::sourceZip.isInitialized) {
//
//                }






                stopSelf()
            }
            catch (e: CancellationException) {
                e.printStackTrace()
                mOffline.status = -2
                mOffline.info = mOffline.info + "${timeToLog()} ${resources.getString(R.string.cancel)}\n"
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                stopSelf()
            }
            catch (e: Exception) {
                e.printStackTrace()
                mOffline.status = -1
                mOffline.info = mOffline.info + "${timeToLog()} ${resources.getString(R.string.error)}: ${e.localizedMessage}\n"
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun sendIntent(mOffline: MOffline) {
        val intent = Intent(ACTION_BROADCAST_OFFLINE)
        intent.putExtra("mOffline", mOffline)
        sendBroadcast(intent)
    }
}
