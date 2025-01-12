package com.avriansyah.githubuserapps.data.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("location")
    val location: String,
)