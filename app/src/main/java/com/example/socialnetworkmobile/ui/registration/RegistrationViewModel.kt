package com.example.socialnetworkmobile.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.socialnetworkmobile.model.authentification.RegisterRequest
import com.example.socialnetworkmobile.model.authentification.RegisterResponse
import com.example.socialnetworkmobile.service.AuthService
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.awaitResponse

class RegistrationViewModel(private val authService: AuthService) : ViewModel() {
    fun register(username: String, email: String, password: String) = liveData(Dispatchers.IO) {
        val request = RegisterRequest(username, email, password)
        try {
            val response = authService.register(request).awaitResponse()
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            } else {
                val errorResponse = response.errorBody()?.string()
                emit(Result.failure<RegisterResponse>(Exception(errorResponse)))
            }
        } catch (e: HttpException) {
            emit(Result.failure<RegisterResponse>(e))
        } catch (e: Exception) {
            emit(Result.failure<RegisterResponse>(e))
        }
    }

}