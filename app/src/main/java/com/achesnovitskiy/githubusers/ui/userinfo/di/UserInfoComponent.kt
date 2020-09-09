package com.achesnovitskiy.githubusers.ui.userinfo.di

import com.achesnovitskiy.githubusers.app.di.AppComponent
import com.achesnovitskiy.githubusers.ui.di.ViewScope
import com.achesnovitskiy.githubusers.ui.userinfo.UserInfoFragment
import dagger.Component

@ViewScope
@Component(dependencies = [AppComponent::class], modules = [UserInfoModule::class])
interface UserInfoComponent {

    fun inject(fragment: UserInfoFragment)
}