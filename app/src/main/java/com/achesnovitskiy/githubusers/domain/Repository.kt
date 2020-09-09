package com.achesnovitskiy.githubusers.domain

import com.achesnovitskiy.githubusers.data.api.Api
import com.achesnovitskiy.githubusers.ui.pojo.UserInfo
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface Repository {
    fun userItemsObservable(): Observable<List<UserItem>>

    fun userInfoObservable(name: String): Observable<UserInfo>
}

class RepositoryImpl @Inject constructor(
    private val api: Api
) : Repository {

    override fun userItemsObservable(): Observable<List<UserItem>> =
        api.getUsers()
            .map { userItemResponses ->
                userItemResponses.map { userItemResponse ->
                    UserItem(
                        name = userItemResponse.name,
                        avatarUrl = userItemResponse.avatarUrl
                    )
                }
            }
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