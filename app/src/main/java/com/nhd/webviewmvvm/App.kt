package com.nhd.webviewmvvm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        private var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}