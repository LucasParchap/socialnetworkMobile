package com.example.socialnetworkmobile.ui.notifications

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.R
import com.example.socialnetworkmobile.model.User
import com.example.socialnetworkmobile.model.friends.AddFriendRequest
import com.example.socialnetworkmobile.model.notifications.Notification
import com.example.socialnetworkmobile.service.FriendsService
import com.example.socialnetworkmobile.service.NotificationsService
import com.example.socialnetworkmobile.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsAdapter(
    private val notifications: MutableList<Notification>,
    private val notificationsService: NotificationsService,
    private val friendsService: FriendsService,
    private val userService: UserService
) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationMessage: TextView = itemView.findViewById(R.id.notification_message)
        val addFriendIcon: ImageView = itemView.findViewById(R.id.add_friend_icon)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val notification = notifications[position]
                    markAsRead(notification.id, position)
                }
            }

            addFriendIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val notification = notifications[position]
                    checkFriendRelation(notification.senderId, itemView.context, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.notificationMessage.text = notification.message

        if (!notification.read) {
            holder.itemView.setBackgroundResource(R.drawable.notification_background_unread)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.notification_background_read)
        }

        if (notification.type == "add-friend") {
            holder.addFriendIcon.visibility = View.VISIBLE
        } else {
            holder.addFriendIcon.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    private fun markAsRead(notificationId: Long, position: Int) {
        notificationsService.markAsRead(notificationId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val updatedNotification = notifications[position].copy(read = true)
                    notifications[position] = updatedNotification
                    notifyItemChanged(position)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NotificationsAdapter", "Error marking notification as read", t)
            }
        })
    }

    private fun checkFriendRelation(senderId: Long, context: Context, position: Int) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1)

        friendsService.hasUserAddedFriend(senderId, userId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    sendFriendRequest(senderId, context, position)
                } else {
                    Toast.makeText(context, "Impossible d'ajouter l'ami, la relation a été supprimée", Toast.LENGTH_SHORT).show()
                    removeNotification(position)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(context, "Erreur lors de la vérification de la relation d'amitié", Toast.LENGTH_SHORT).show()
                removeNotification(position)
            }
        })
    }

    private fun sendFriendRequest(senderId: Long, context: Context, position: Int) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1)
        val request = AddFriendRequest(userId, senderId)

        friendsService.addFriend(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    userService.getUserDetails(senderId).enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                val sender = response.body()
                                Toast.makeText(context, "${sender?.username} ajouté comme ami", Toast.LENGTH_SHORT).show()
                                removeNotification(position)
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.e("NotificationsAdapter", "Error fetching sender details", t)
                        }
                    })
                } else {
                    Toast.makeText(context, "Impossible d'ajouter l'ami, la relation existe déjà.", Toast.LENGTH_SHORT).show()
                    removeNotification(position)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NotificationsAdapter", "Error sending friend request", t)
                removeNotification(position)
            }
        })
    }

    fun removeNotification(position: Int) {
        val notificationId = notifications[position].id
        notificationsService.deleteNotification(notificationId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    notifications.removeAt(position)
                    notifyItemRemoved(position)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("NotificationsAdapter", "Error deleting notification", t)
            }
        })
    }
}
