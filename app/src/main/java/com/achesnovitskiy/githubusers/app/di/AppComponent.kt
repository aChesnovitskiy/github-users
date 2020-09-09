package com.achesnovitskiy.githubusers.app.di

import android.content.Context
import com.achesnovitskiy.githubusers.data.api.Api
import com.achesnovitskiy.githubusers.data.di.DataModule
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.domain.di.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    val context: Context

    val repository: Repository

    val api: Api
}