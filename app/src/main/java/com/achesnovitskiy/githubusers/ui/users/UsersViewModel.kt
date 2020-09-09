package com.achesnovitskiy.githubusers.ui.users

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.achesnovitskiy.githubusers.R
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface UsersViewModel {

    val usersObservable: Observable<List<UserItem>>

    val loadingStateObservable: Observable<LoadingState>

    val loadObserver: Observer<Unit>
}

class UsersViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), UsersViewModel {

    private val loadingStateBehaviorSubject: BehaviorSubject<LoadingState> =
        BehaviorSubject.create()

    override val usersObservable: Observable<List<UserItem>>
        get() = repository.userItemsObservable

    override val loadingStateObservable: Observable<LoadingState>
        get() = loadingStateBehaviorSubject

    override val loadObserver: PublishSubject<Unit> = PublishSubject.create()

    init {
        loadObserver
            .switchMap {
                repository.loadCompletable
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
                            errorRes = R.string.users_loading_error_message
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