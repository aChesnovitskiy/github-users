package com.achesnovitskiy.githubusers.ui.userinfo

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.achesnovitskiy.githubusers.R
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.ui.pojo.UserInfo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface UserInfoViewModel {

    val userInfoObservable: Observable<UserInfo>

    val loadingStateObservable: Observable<LoadingState>

    val loadUserInfoObserver: Observer<String>
}

class UserInfoViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), UserInfoViewModel {

    private val loadingStateBehaviorSubject: BehaviorSubject<LoadingState> =
        BehaviorSubject.create()

    override val userInfoObservable: Observable<UserInfo> =
        repository.userInfoObservable

    override val loadingStateObservable: Observable<LoadingState>
        get() = loadingStateBehaviorSubject

    override val loadUserInfoObserver: PublishSubject<String> = PublishSubject.create()

    init {
        loadUserInfoObserver
            .switchMap {name ->
                repository.loadUserInfoCompletable(name)
                    .andThen(
                        Observable.just(
                            LoadingState(
                                isLoading = false,
                                errorRes = null
                            )
                        )
                    )
                    .startWith(
                        LoadingState(
                            isLoading = true,
                            errorRes = null
                        )
                    )
                    .onErrorReturnItem(
                        LoadingState(
                            isLoading = false,
                            errorRes = R.string.loading_error_message
                        )
                    )
            }
            .subscribeOn(Schedulers.io())
            .subscribe(loadingStateBehaviorSubject)
    }
}

data class LoadingState(
    val isLoading: Boolean,
    @StringRes val errorRes: Int?
)