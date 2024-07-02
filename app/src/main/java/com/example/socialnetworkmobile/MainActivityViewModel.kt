package com.example.socialnetworkmobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.socialnetworkmobile.model.User
import com.example.socialnetworkmobile.service.UserService
import kotlinx.coroutines.Dispatchers
import retrofit2.awaitResponse

class MainActivityViewModel(private val userService: UserService) : ViewModel() {

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    private val _userDetails = MutableLiveData<User>()
    val userDetails: LiveData<User> = _userDetails

    fun getUserId(username: String) {
        liveData(Dispatchers.IO) {
            try {
                val response = userService.getUserId(username).awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(it)
                        _userId.postValue(it)
                    } ?: run {
                        emit(null)
                    }
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                emit(null)
            }
        }.observeForever { userId ->
            userId?.let {
                _userId.postValue(it)
            }
        }
    }

    fun getUserDetails(id: Long) {
        liveData(Dispatchers.IO) {
            try {
                val response = userService.getUserDetails(id).awaitResponse()
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(it)
                        _userDetails.postValue(it)
                    } ?: run {
                        emit(null)
                    }
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                emit(null)
            }
        }.observeForever { userDetails ->
            userDetails?.let {
                _userDetails.postValue(it)
            }
        }
    }
}