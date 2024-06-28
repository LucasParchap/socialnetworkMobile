package com.example.socialnetworkmobile

import android.app.Application
import com.example.socialnetworkmobile.di.injectModuleDependencies
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        injectModuleDependencies(this@App)
    }
}