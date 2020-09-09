package com.achesnovitskiy.githubusers.app

import android.app.Application
import com.achesnovitskiy.githubusers.app.di.AppComponent
import com.achesnovitskiy.githubusers.app.di.AppModule
import com.achesnovitskiy.githubusers.app.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(
                AppModule(context = this)
            )
            .build()
    }
}