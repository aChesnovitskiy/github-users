package com.achesnovitskiy.githubusers.ui.users

import android.util.Log
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

    val loadUsersObserver: Observer<Unit>
}

class UsersViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), UsersViewModel {

    private var lastLoadedUserId: Int = 0

    private val loadingStateBehaviorSubject: BehaviorSubject<LoadingState> =
        BehaviorSubject.create()

    override val usersObservable: Observable<List<UserItem>>
        get() = repository.userItemsObservable
            .map { userItems ->
                lastLoadedUserId = userItems.last().id

                userItems
            }

    override val loadingStateObservable: Observable<LoadingState>
        get() = loadingStateBehaviorSubject

    override val loadUsersObserver: PublishSubject<Unit> = PublishSubject.create()

    init {
        loadUsersObserver
            .switchMap {
                repository.loadUsersCompletable(lastLoadedUserId)
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
                    .doOnError {
                        Log.e("My_UsersViewModel", it.message, it)
                    }
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