package com.camelsoft.trademonitor._presentation.services

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OfflineService : LifecycleService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("srv", "onCreate")
    }

    var bool:Boolean = false

    override fun onDestroy() {
        Log.d("srv", "onDestroy")
        super.onDestroy()
    }

    init {
        Log.d("srv", "init")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        lifecycleScope.launch(Dispatchers.IO) {
            intent?.let { Log.d("srv", "onStartCommand: Intent action: ${ it.action}") }
            Log.d("srv", "onStartCommand: flags: $flags")
            Log.d("srv", "onStartCommand: startId: $startId")

            while (true) {
                Log.d("srv", "Бесконечный цикл - flags: $flags - startId: $startId")
                delay(1000)
            }

        }

        return super.onStartCommand(intent, flags, startId)
    }


    //    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        intent?.let { Log.d("srv", "onStartCommand: Intent action: ${ it.action}") }
//        Log.d("srv", "onStartCommand: flags: $flags")
//        Log.d("srv", "onStartCommand: startId: $startId")
//
////        GlobalScope.launch(Dispatchers.IO) {
//            while (true) {
//                Log.d("srv", "Бесконечный цикл - flags: $flags - startId: $startId")
//                delay(1000)
//            }
////        }
//
//        Thread {
//            while (true) {
//                Log.d("srv", "Бесконечный цикл - flags: $flags - startId: $startId")
//                Thread.sleep(1000)
//            }
//
//        }.start()
//
//
//
//        return START_NOT_STICKY
//    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("srv", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.d("srv", "onRebind")
        super.onRebind(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("srv", "onTaskRemoved")
        super.onTaskRemoved(rootIntent)
    }
}
