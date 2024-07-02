package com.example.socialnetworkmobile.di.modules

import com.example.socialnetworkmobile.network.AuthInterceptor
import com.example.socialnetworkmobile.service.AuthService
import com.example.socialnetworkmobile.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal val networkModule = module {
    single {
        OkHttpClient.Builder().apply {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
            addInterceptor(AuthInterceptor(get()))
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(AuthService::class.java)
    }
    single<UserService> {
        get<Retrofit>().create(UserService::class.java)
    }
}