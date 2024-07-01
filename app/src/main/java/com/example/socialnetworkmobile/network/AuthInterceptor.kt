package com.example.socialnetworkmobile.network

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.socialnetworkmobile.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val context: Context) : Interceptor {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getString("jwt_token", null)
        val request = chain.request().newBuilder()

        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        val response = chain.proceed(request.build())

        if (response.code == 401 || response.code == 403) {
            Log.d("AuthInterceptor", "Unauthorized or Forbidden - Redirecting to login")
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
        return response
    }
}