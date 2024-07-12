package com.example.socialnetworkmobile.service

import com.example.socialnetworkmobile.model.friends.AddFriendRequest
import com.example.socialnetworkmobile.model.friends.FriendDTO
import com.example.socialnetworkmobile.model.friends.RemoveFriendRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendsService {
    @GET("/api/friends/list")
    fun getFriends(@Query("userId") userId: Long): Call<List<FriendDTO>>

    @HTTP(method = "DELETE", path = "/api/friends/remove", hasBody = true)
    fun removeFriend(@Body request: RemoveFriendRequest): Call<Void>

    @GET("/api/friends/non-friends")
    fun getNonFriends(@Query("userId") userId: Long): Call<List<FriendDTO>>

    @POST("/api/friends/add")
    fun addFriend(@Body request: AddFriendRequest): Call<Void>

    @GET("/api/friend-relation/has-user-added-friend")
    fun hasUserAddedFriend(@Query("userId") userId: Long, @Query("friendId") friendId: Long): Call<Boolean>

    @GET("/api/friend-relation/are-both-friends")
    fun areBothFriends(@Query("userId") userId: Long, @Query("friendId") friendId: Long): Call<Boolean>

    @GET("/api/friends/pending-requests")
    fun getPendingFriendRequests(@Query("userId") userId: Long): Call<List<FriendDTO>>
}