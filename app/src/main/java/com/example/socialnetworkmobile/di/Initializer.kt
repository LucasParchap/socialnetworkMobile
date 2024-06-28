package com.example.socialnetworkmobile.di

import android.content.Context
import com.example.socialnetworkmobile.di.modules.ViewModels
import com.example.socialnetworkmobile.di.modules.networkModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.error.ApplicationAlreadyStartedException


fun injectModuleDependencies(context: Context) {
    try {
        startKoin {
            androidContext(context)
            modules(modules)
        }
    } catch (alreadyStart: ApplicationAlreadyStartedException ) {
        loadKoinModules(modules)
    }
}
private val modules = mutableListOf(ViewModels,networkModule)
