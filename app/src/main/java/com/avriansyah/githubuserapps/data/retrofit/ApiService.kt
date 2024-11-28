package com.avriansyah.githubuserapps.data.retrofit

import com.avriansyah.githubuserapps.data.response.DetailResponse
import com.avriansyah.githubuserapps.data.response.ItemsItem
import com.avriansyah.githubuserapps.data.response.ResponseUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("users")
    fun getUsers(): Call<ArrayList<ItemsItem>>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("search/users")
    fun findUsers(
        @Query("q") q: String
    ): Call<ResponseUsers>

    @GET("users/{username}/following")
    fun getUserFollowings(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

}