package com.example.socialnetworkmobile.service
import com.example.socialnetworkmobile.model.notifications.Notification
import com.example.socialnetworkmobile.model.notifications.NotificationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationsService {
    @GET("/api/notifications/{userId}")
    fun getNotifications(@Path("userId") userId: Long): Call<List<Notification>>

    @DELETE("/api/notifications/{notificationId}")
    fun deleteNotification(@Path("notificationId") notificationId: Long): Call<Void>

    @POST("/api/notifications/read/{notificationId}")
    fun markAsRead(@Path("notificationId") notificationId: Long): Call<Void>

    @GET("/api/notifications/{userId}/unread")
    fun getUnreadNotifications(@Path("userId") userId: Long): Call<List<Notification>>

    @POST("/api/notifications/{userId}")
    fun createNotification(@Path("userId") userId: Long, @Body notificationRequest: NotificationRequest): Call<Void>
}