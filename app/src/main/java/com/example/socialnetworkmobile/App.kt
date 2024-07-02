package com.example.socialnetworkmobile

import android.app.Application
import com.example.socialnetworkmobile.di.injectModuleDependencies

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        injectModuleDependencies(this@App)
    }
}