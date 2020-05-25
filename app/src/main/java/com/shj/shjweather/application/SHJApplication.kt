package com.shj.shjweather.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SHJApplication : Application() {
    companion object {
        //        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "H41H8l7o3nxl1iGK"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}