package com.vfx.rightbrainstudios.smartiotapplication

import android.app.Application
import com.vfx.rightbrainstudios.smartiotapplication.util.GlobalExceptionHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartIotApplication: Application(){

    override fun onCreate() {
        super.onCreate()

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            GlobalExceptionHandler(this, defaultHandler)
        )
    }
}