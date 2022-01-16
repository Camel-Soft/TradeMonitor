package com.camelsoft.trademonitor.common

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    init {
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null

        fun getAppContext() : Context {
            return appInstance!!.applicationContext
        }
    }
}