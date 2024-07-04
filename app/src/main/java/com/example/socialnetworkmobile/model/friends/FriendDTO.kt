package com.example.socialnetworkmobile.model.friends

data class FriendDTO(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Set<String>,
    var isPending: Boolean = false
)
