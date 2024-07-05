package com.example.socialnetworkmobile.model.notifications

data class NotificationRequest(
    val type: String,
    val message: String,
    val senderId: Long
)