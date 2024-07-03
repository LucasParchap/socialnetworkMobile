package com.example.socialnetworkmobile.ui.friends

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.R
import com.example.socialnetworkmobile.model.friends.FriendDTO
import com.example.socialnetworkmobile.model.friends.RemoveFriendRequest
import com.example.socialnetworkmobile.service.FriendsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsAdapter(
    private val context: Context,
    private var friendsList: MutableList<FriendDTO>,
    private val friendService: FriendsService
) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    private var filteredList: MutableList<FriendDTO> = friendsList.toMutableList()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val userId: Long = sharedPreferences.getLong("user_id", -1)

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.friend_username)
        private val emailTextView: TextView = itemView.findViewById(R.id.friend_email)
        private val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(friend: FriendDTO) {
            usernameTextView.text = friend.username
            emailTextView.text = friend.email

            deleteIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeFriend(friend.id, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            friendsList
        } else {
            friendsList.filter { it.username.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    private fun removeFriend(friendId: Long, position: Int) {
        val request = RemoveFriendRequest(userId, friendId)

        friendService.removeFriend(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    removeItem(position)
                } else {
                    Log.e("FriendsAdapter", "Failed to remove friend: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("FriendsAdapter", "Error removing friend", t)
            }
        })
    }

    private fun removeItem(position: Int) {
        val friendToRemove = filteredList[position]
        filteredList.removeAt(position)
        notifyItemRemoved(position)
        friendsList.remove(friendToRemove)
    }

    fun updateList(newFriendsList: MutableList<FriendDTO>) {
        friendsList = newFriendsList
        filter("")
    }
}