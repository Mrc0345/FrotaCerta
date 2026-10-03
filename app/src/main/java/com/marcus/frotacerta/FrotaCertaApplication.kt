package com.marcus.frotacerta

import android.app.Application
import com.marcus.frotacerta.data.di.AppContainer

class FrotaCertaApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)
    }
}
