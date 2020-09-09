package com.achesnovitskiy.githubusers.ui.users.di

import com.achesnovitskiy.githubusers.app.di.AppComponent
import com.achesnovitskiy.githubusers.ui.di.ViewScope
import com.achesnovitskiy.githubusers.ui.users.UsersFragment
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [UsersModule::class])
interface UsersComponent {

    fun inject(fragment: UsersFragment)
}