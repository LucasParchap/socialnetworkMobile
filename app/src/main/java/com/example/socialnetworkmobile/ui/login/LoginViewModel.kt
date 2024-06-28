package com.example.socialnetworkmobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.socialnetworkmobile.model.authentification.LoginRequest
import com.example.socialnetworkmobile.service.AuthService
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class LoginViewModel(private val authService: AuthService) : ViewModel() {

    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        val request = LoginRequest(username, password)
        try {
            val response = authService.login(request).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.jwt?.let {
                    emit(Result.success(it))
                } ?: run {
                    emit(Result.failure<Throwable>(Exception("Failed to retrieve JWT")))
                }
            } else {
                emit(Result.failure<Throwable>(Exception("Login failed!")))
            }
        } catch (e: HttpException) {
            emit(Result.failure<Throwable>(Exception("Login failed!")))
        } catch (e: IOException) {
            emit(Result.failure<Throwable>(Exception("Network ERROR")))
        } catch (e: Exception) {
            emit(Result.failure<Throwable>(Exception("ERROR!")))
        }
    }
}