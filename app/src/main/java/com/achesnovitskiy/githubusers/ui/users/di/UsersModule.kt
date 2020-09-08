package com.achesnovitskiy.githubusers.ui.users.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.ui.di.ViewScope
import com.achesnovitskiy.githubusers.ui.users.UsersViewModel
import com.achesnovitskiy.githubusers.ui.users.UsersViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class UsersModule(
    private val viewModelStoreOwner: ViewModelStoreOwner
) {

    @Provides
    @ViewScope
    fun provideUsersViewModel(repository: Repository): UsersViewModel =
        ViewModelProvider(
            viewModelStoreOwner,
            UsersViewModelFactory(repository)
        ).get(UsersViewModelImpl::class.java)

    class UsersViewModelFactory(private val repository: Repository) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            UsersViewModelImpl(repository) as T
    }
}