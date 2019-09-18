package com.zuluft.mvvm.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

abstract class MvvmApplication :
    Application() {


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(provideModules())
        }
    }

    abstract fun provideModules(): List<Module>

}

