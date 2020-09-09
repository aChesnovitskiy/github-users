package com.achesnovitskiy.githubusers.ui.userinfo

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.ui.pojo.UserInfo
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface UserInfoViewModel {

    fun getUserInfoObservable(name: String): Observable<UserInfo>

    val loadingStateObservable: Observable<LoadingState>
}

class UserInfoViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), UserInfoViewModel {

    private val loadingStateBehaviorSubject: BehaviorSubject<LoadingState> =
        BehaviorSubject.create()

    override val loadingStateObservable: Observable<LoadingState>
        get() = loadingStateBehaviorSubject

    override fun getUserInfoObservable(name: String): Observable<UserInfo> =
        repository.userInfoObservable(name)
}

data class LoadingState(
    val isLoading: Boolean,
    @StringRes val errorRes: Int?
)