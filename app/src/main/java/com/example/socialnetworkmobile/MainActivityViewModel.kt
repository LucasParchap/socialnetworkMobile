package com.example.socialnetworkmobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.socialnetworkmobile.model.User
import com.example.socialnetworkmobile.service.FriendsService
import com.example.socialnetworkmobile.service.UserService
import kotlinx.coroutines.Dispatchers
import retrofit2.awaitResponse

class MainActivityViewModel(private val userService: UserService, private val friendsService: FriendsService) : ViewModel() {

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> = _userId

    private val _userDetails = MutableLiveData<User>()
    val userDetails: LiveData<User> = _userDetails

    private val _friendCount = MutableLiveData<Int>()
    val friendCount: LiveData<Int> = _friendCount

    private val _subscriptionCount = MutableLiveData<Int>()
    val subscriptionCount: LiveData<Int> = _subscriptionCount

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
    fun getFriendCount(userId: Long) {
        liveData(Dispatchers.IO) {
            try {
                val response = friendsService.getFriends(userId).awaitResponse()
                if (response.isSuccessful) {
                    emit(response.body()?.size ?: 0)
                } else {
                    emit(0)
                }
            } catch (e: Exception) {
                emit(0)
            }
        }.observeForever { count ->
            _friendCount.postValue(count)
        }
    }

    fun getSubscriptionCount(userId: Long) {
        liveData(Dispatchers.IO) {
            try {
                val response = friendsService.getPendingFriendRequests(userId).awaitResponse()
                if (response.isSuccessful) {
                    emit(response.body()?.size ?: 0)
                } else {
                    emit(0)
                }
            } catch (e: Exception) {
                emit(0)
            }
        }.observeForever { count ->
            _subscriptionCount.postValue(count)
        }
    }
}