package com.achesnovitskiy.githubusers.data.api

import io.reactivex.Observable
import retrofit2.http.*

interface Api {

    @Headers("accept: application/vnd.github.v3+json")
    @GET("users")
    fun getUsers(): Observable<List<UserResponse>>
}