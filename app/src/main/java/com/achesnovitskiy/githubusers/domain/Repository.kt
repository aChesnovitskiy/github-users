package com.achesnovitskiy.githubusers.domain

import com.achesnovitskiy.githubusers.data.api.Api
import com.achesnovitskiy.githubusers.ui.pojo.UserInfo
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

interface Repository {
    val userItemsObservable: Observable<List<UserItem>>

    val loadCompletable: Completable

    fun userInfoObservable(name: String): Observable<UserInfo>
}

class RepositoryImpl @Inject constructor(
    private val api: Api
) : Repository {

    private val userItemsObservableBehaviorSubject: BehaviorSubject<List<UserItem>> =
        BehaviorSubject.create()

    override val userItemsObservable: Observable<List<UserItem>>
        get() = userItemsObservableBehaviorSubject

    override val loadCompletable: Completable =
        api.getUsers()
            .map { userItemResponses ->
                userItemResponses.map { userItemResponse ->
                    UserItem(
                        name = userItemResponse.name,
                        avatarUrl = userItemResponse.avatarUrl
                    )
                }
            }
            .map {
                userItemsObservableBehaviorSubject.onNext(it)
            }
            .ignoreElements()
            .subscribeOn(Schedulers.io())

    override fun userInfoObservable(name: String): Observable<UserInfo> =
        api.getUserInfo(name)
            .map { userResponse ->
                UserInfo(
                    name = userResponse.name,
                    avatarUrl = userResponse.avatarUrl,
                    webpage = userResponse.webpage,
                    location = userResponse.location
                )
            }
            .subscribeOn(Schedulers.io())
}