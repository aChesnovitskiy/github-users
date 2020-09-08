package com.achesnovitskiy.githubusers.domain

import com.achesnovitskiy.githubusers.data.api.Api
import com.achesnovitskiy.githubusers.ui.pojo.UserItem
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface Repository {
    val usersObservable: Observable<List<UserItem>>
}

class RepositoryImpl @Inject constructor(
    private val api: Api
) : Repository {

    override val usersObservable: Observable<List<UserItem>>
        get() = api.getUsers()
            .map { userResponses ->
                userResponses.map {userResponse ->
                    UserItem(
                        name = userResponse.name,
                        avatarUrl = userResponse.avatarUrl
                    )
                }
            }
            .subscribeOn(Schedulers.io())
}