package com.countrydelight.cdpreferencehelper.base

import android.app.Application
import android.os.StrictMode
import com.countrydelight.cdpreferencehelper.BuildConfig

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initStrictCheck()
    }

    private fun initStrictCheck() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }
}