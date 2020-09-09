package com.achesnovitskiy.githubusers.data.api

import com.google.gson.annotations.SerializedName

data class UserItemResponse(
    @SerializedName("login") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("avatar_url") val avatarUrl: String
)