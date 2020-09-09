package com.achesnovitskiy.githubusers.data.api

import io.reactivex.Observable
import retrofit2.http.*

interface Api {

    @GET("users")
    fun getUsers(): Observable<List<UserItemResponse>>

    @GET("users/{username}")
    fun getUserInfo(@Path("username") name: String): Observable<UserInfoResponse>
}