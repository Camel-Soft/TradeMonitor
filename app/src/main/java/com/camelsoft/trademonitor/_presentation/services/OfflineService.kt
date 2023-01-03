package com.camelsoft.trademonitor._presentation.services

import android.content.Intent
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
    private lateinit var sourceFolder: File
    private lateinit var publishFolder: File
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

                // Стадия 1 - Копирование   ********************************************************
                mOffline = MOffline(
                    status = 1,
                    info = "${timeToLog()}     ${resources.getString(R.string.stage_copy)}\n",
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
                            mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.info)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Error -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Progress -> {
                            mOffline.stageName = it.stage
                            mOffline.stagePercent = it.percent
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                        }
                    }
                }
                if (mOffline.status < 0) return@launch

                // Стадия 2 - Распаковка   *********************************************************
                mOffline.status = 1
                mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.stage_unpack)}\n"
                mOffline.stageCurrent = 2
                mOffline.stageTotal = 3
                mOffline.stageName = resources.getString(R.string.stage_unpack)
                mOffline.stagePercent = 0
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                if (!this@OfflineService::sourceZip.isInitialized) {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${resources.getString(R.string.error_arc_file)}\n"
                    settings.putMOffline(mOffline)
                    sendIntent(mOffline)
                    stopSelf()
                    return@launch
                }
                if (!sourceZip.exists() || !sourceZip.isFile) {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${resources.getString(R.string.error_file_not_found)} - ${sourceZip.name}\n"
                    settings.putMOffline(mOffline)
                    sendIntent(mOffline)
                    stopSelf()
                    return@launch
                }
                iOfflBase.unzipOfflBase(zipFile = sourceZip).collect {
                    when (it) {
                        is EventsProgress.Success -> {
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            sourceFolder = it.data
                        }
                        is EventsProgress.UnSuccess -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.info)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Error -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Progress -> {
                            mOffline.stageName = it.stage
                            mOffline.stagePercent = it.percent
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                        }
                    }
                }
                if (mOffline.status < 0) return@launch

                // Стадия 3 - Публикация ***********************************************************
                mOffline.status = 1
                mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.stage_public)}\n"
                mOffline.stageCurrent = 3
                mOffline.stageTotal = 3
                mOffline.stageName = resources.getString(R.string.stage_public)
                mOffline.stagePercent = 0
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                if (!this@OfflineService::sourceFolder.isInitialized) {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${resources.getString(R.string.error_folder_unzip)}\n"
                    settings.putMOffline(mOffline)
                    sendIntent(mOffline)
                    stopSelf()
                    return@launch
                }
                if (!sourceFolder.exists() || !sourceFolder.isDirectory) {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${resources.getString(R.string.error_folder_not_found)} - ${sourceFolder.name}\n"
                    settings.putMOffline(mOffline)
                    sendIntent(mOffline)
                    stopSelf()
                    return@launch
                }
                iOfflBase.publishOfflBase(sourceFolder = sourceFolder).collect {
                    when (it) {
                        is EventsProgress.Success -> {
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            publishFolder = it.data
                        }
                        is EventsProgress.UnSuccess -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.info)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Error -> {
                            mOffline.status = -1
                            mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${it.message}\n"
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                            stopSelf()
                        }
                        is EventsProgress.Progress -> {
                            mOffline.stageName = it.stage
                            mOffline.stagePercent = it.percent
                            settings.putMOffline(mOffline)
                            sendIntent(mOffline)
                        }
                    }
                }
                if (mOffline.status < 0) return@launch

                // Показ списка файлов с размером **************************************************
                if (!this@OfflineService::publishFolder.isInitialized) {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${resources.getString(R.string.error_folder_publish)}\n"
                    settings.putMOffline(mOffline)
                    sendIntent(mOffline)
                    stopSelf()
                    return@launch
                }
                if (!publishFolder.exists() || !publishFolder.isDirectory) {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${resources.getString(R.string.error_folder_not_found)} - ${publishFolder.name}\n"
                    settings.putMOffline(mOffline)
                    sendIntent(mOffline)
                    stopSelf()
                    return@launch
                }
                mOffline.info = mOffline.info + "\n"
                var scandbf = false
                var scanndx = false
                publishFolder.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        mOffline.info = mOffline.info + "${timeToLog()}     ${file.name} - ${file.length()} byte\n"
                        if (file.name == "scan.dbf") scandbf = true
                        if (file.name == "scan.ndx") scanndx = true
                    }
                }
                mOffline.info = mOffline.info + "\n"
                if (scandbf && scanndx) {
                    mOffline.status = 0
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.offlsource_download_success)}\n"
                }
                else {
                    mOffline.status = -1
                    mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.offlsource_download_unsuccess)}\n"
                }
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                stopSelf()
                return@launch
            }
            catch (e: CancellationException) {
                e.printStackTrace()
                mOffline.status = -2
                mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.cancel1)}\n"
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                stopSelf()
                return@launch
            }
            catch (e: Exception) {
                e.printStackTrace()
                mOffline.status = -1
                mOffline.info = mOffline.info + "${timeToLog()}     ${resources.getString(R.string.error)}: ${e.localizedMessage}\n"
                settings.putMOffline(mOffline)
                sendIntent(mOffline)
                stopSelf()
                return@launch
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
