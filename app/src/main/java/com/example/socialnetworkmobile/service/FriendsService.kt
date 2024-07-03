package com.example.socialnetworkmobile.service

import com.example.socialnetworkmobile.model.friends.FriendDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface FriendService {
    @GET("/api/friends/list")
    fun getFriends(@Query("userId") userId: Long): Call<List<FriendDTO>>
}