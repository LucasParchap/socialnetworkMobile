package com.example.socialnetworkmobile.model.friends

data class RemoveFriendRequest(
    val userId: Long,
    val friendId: Long
)