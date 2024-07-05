package com.example.socialnetworkmobile.model.notifications

data class Notification(
    val id: Long,
    val type: String,
    val message: String,
    val read: Boolean,
    val senderId: Long
)
