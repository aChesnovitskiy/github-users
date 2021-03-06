package com.achesnovitskiy.githubusers.domain

import android.util.Log
import com.achesnovitskiy.githubusers.data.api.Api
import com.achesnovitskiy.githubusers.ui.pojo.UserInfo
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface Repository {
    val userItemsObservable: Observable<List<UserItem>>

    val userInfoObservable: Observable<UserInfo>

    fun loadUsersCompletable(userId: Int): Completable

    fun loadUserInfoCompletable(name: String): Completable
}

class RepositoryImpl @Inject constructor(
    private val api: Api
) : Repository {

    private val userItems: MutableList<UserItem> = mutableListOf()

    private val userItemsBehaviorSubject: BehaviorSubject<List<UserItem>> = BehaviorSubject.create()

    private val userInfoPublishSubject: PublishSubject<UserInfo> = PublishSubject.create()

    override val userItemsObservable: Observable<List<UserItem>>
        get() = userItemsBehaviorSubject

    override val userInfoObservable: Observable<UserInfo>
        get() = userInfoPublishSubject

    override fun loadUsersCompletable(userId: Int): Completable =
        api.getUsers(userId)
            .map { userItemResponses ->
                userItems.addAll(
                    userItemResponses.map { userItemResponse ->
                        UserItem(
                            name = userItemResponse.name,
                            id = userItemResponse.id,
                            avatarUrl = userItemResponse.avatarUrl
                        ).also {
                            Log.d("My_", it.toString())
                        }
                    }
                )

                userItemsBehaviorSubject.onNext(userItems)
            }
            .ignoreElements()
            .subscribeOn(Schedulers.io())

    override fun loadUserInfoCompletable(name: String): Completable =
        api.getUserInfo(name)
            .map { userResponse ->
                userInfoPublishSubject.onNext(
                    UserInfo(
                        name = userResponse.name,
                        avatarUrl = userResponse.avatarUrl,
                        webpage = userResponse.webpage,
                        location = userResponse.location
                    )
                )
            }
            .ignoreElements()
            .subscribeOn(Schedulers.io())
}