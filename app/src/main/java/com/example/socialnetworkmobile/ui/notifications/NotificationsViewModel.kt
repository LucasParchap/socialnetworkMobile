package com.example.socialnetworkmobile.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetworkmobile.model.notifications.Notification
import com.example.socialnetworkmobile.service.NotificationsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel(private val notificationsService: NotificationsService) : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    fun loadNotifications(userId: Long) {
        notificationsService.getNotifications(userId).enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if (response.isSuccessful) {
                    _notifications.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
            }
        })
    }
}
