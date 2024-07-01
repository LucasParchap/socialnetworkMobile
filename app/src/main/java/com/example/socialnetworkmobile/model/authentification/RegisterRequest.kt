package com.example.socialnetworkmobile.model.authentification

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)