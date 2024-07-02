package com.example.socialnetworkmobile.service

import com.example.socialnetworkmobile.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/api/user/{username}/id")
    fun getUserId(@Path("username") username: String): Call<Long>

    @GET("/api/user/{id}/details")
    fun getUserDetails(@Path("id") id: Long): Call<User>
}
