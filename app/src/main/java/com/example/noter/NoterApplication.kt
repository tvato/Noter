package com.example.noter

import android.app.Application
import com.example.noter.data.AppContainer
import com.example.noter.data.AppDataContainer

class NoterApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}