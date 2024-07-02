package com.example.socialnetworkmobile.service


import com.example.socialnetworkmobile.model.authentification.AuthResponse
import com.example.socialnetworkmobile.model.authentification.LoginRequest
import com.example.socialnetworkmobile.model.authentification.RegisterRequest
import com.example.socialnetworkmobile.model.authentification.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>
    @POST("/api/auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}