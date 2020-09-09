package com.achesnovitskiy.githubusers.domain.di

import com.achesnovitskiy.githubusers.data.api.Api
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.domain.RepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: Api): Repository = RepositoryImpl(api)
}