package com.example.socialnetworkmobile.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworkmobile.model.friends.FriendDTO
import com.example.socialnetworkmobile.service.FriendsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class FriendsViewModel(private val friendsService: FriendsService) : ViewModel() {

    private val _friendsList = MutableLiveData<List<FriendDTO>>()
    val friendsList: LiveData<List<FriendDTO>> = _friendsList

    private val _nonFriendsList = MutableLiveData<List<FriendDTO>>()
    val nonFriendsList: LiveData<List<FriendDTO>> get() = _nonFriendsList

    fun fetchFriends(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<List<FriendDTO>> = friendsService.getFriends(userId).execute()
                if (response.isSuccessful) {
                    _friendsList.postValue(response.body())
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }
    fun fetchNonFriends(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<List<FriendDTO>> = friendsService.getNonFriends(userId).execute()
                if (response.isSuccessful) {
                    _nonFriendsList.postValue(response.body())
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }
}