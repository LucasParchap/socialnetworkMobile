package com.example.socialnetworkmobile.model

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String,
    val roles: Set<String>
)