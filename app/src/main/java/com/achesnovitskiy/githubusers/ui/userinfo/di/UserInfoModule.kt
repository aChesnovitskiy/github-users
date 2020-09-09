package com.achesnovitskiy.githubusers.ui.userinfo.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.ui.di.ViewScope
import com.achesnovitskiy.githubusers.ui.userinfo.UserInfoViewModel
import com.achesnovitskiy.githubusers.ui.userinfo.UserInfoViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class UserInfoModule(
    private val viewModelStoreOwner: ViewModelStoreOwner
) {

    @Provides
    @ViewScope
    fun provideUserInfoViewModel(repository: Repository): UserInfoViewModel =
        ViewModelProvider(
            viewModelStoreOwner,
            UserInfoViewModelFactory(repository)
        ).get(UserInfoViewModelImpl::class.java)

    class UserInfoViewModelFactory(private val repository: Repository) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            UserInfoViewModelImpl(repository) as T
    }
}