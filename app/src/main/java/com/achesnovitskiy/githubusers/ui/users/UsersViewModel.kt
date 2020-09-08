package com.achesnovitskiy.githubusers.ui.users

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.achesnovitskiy.githubusers.domain.Repository
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface UsersViewModel {

    val usersObservable: Observable<List<UserItem>>

    val loadingStateObservable: Observable<LoadingState>

    val refreshObserver: Observer<Unit>
}

class UsersViewModelImpl @Inject constructor(private val repository: Repository) :
    ViewModel(), UsersViewModel {

    private val usersBehaviorSubject: BehaviorSubject<List<UserItem>> = BehaviorSubject.create()

    private val loadingStateBehaviorSubject: BehaviorSubject<LoadingState> =
        BehaviorSubject.create()

    override val usersObservable: Observable<List<UserItem>>
                get() = repository.usersObservable
//        get() = Observable.just(
//            listOf(
//                UserItem("1", "1"),
//                UserItem("2", "2")
//            )
//        )

    override val loadingStateObservable: Observable<LoadingState>
        get() = loadingStateBehaviorSubject

    override val refreshObserver: PublishSubject<Unit> = PublishSubject.create()

//    init {
//        repository.reposObservable
//            .subscribeOn(Schedulers.io())
//            .subscribe(usersBehaviorSubject)
//
//        refreshObserver
//            .switchMap {
//                repository.refreshCompletable
//                    .andThen(
//                        Observable.just(
//                            LoadingState(
//                                isLoading = false,
//                                errorRes = null
//                            )
//                        )
//                    )
//                    .startWith(
//                        LoadingState(
//                            isLoading = true,
//                            errorRes = null
//                        )
//                    )
//                    .onErrorReturnItem(
//                        LoadingState(
//                            isLoading = false,
//                            errorRes = R.string.users_loading_error_message
//                        )
//                    )
//            }
//            .subscribeOn(Schedulers.io())
//            .subscribe(loadingStateBehaviorSubject)
//    }
}

data class LoadingState(
    val isLoading: Boolean,
    @StringRes val errorRes: Int?
)