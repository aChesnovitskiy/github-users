package com.achesnovitskiy.githubusers.data.api

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("login") val name: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val webpage: String,
    @SerializedName("location") val location: String
)