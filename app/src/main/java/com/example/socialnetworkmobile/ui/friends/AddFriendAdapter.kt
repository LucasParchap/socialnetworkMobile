package com.example.socialnetworkmobile.ui.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.R
import com.example.socialnetworkmobile.model.friends.FriendDTO
import com.example.socialnetworkmobile.model.friends.AddFriendRequest
import com.example.socialnetworkmobile.service.FriendsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFriendAdapter(
    private val context: Context,
    private var userList: MutableList<FriendDTO>,
    private val friendService: FriendsService
) : RecyclerView.Adapter<AddFriendAdapter.UserViewHolder>() {

    private var filteredList: MutableList<FriendDTO> = userList.toMutableList()
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val userId = sharedPreferences.getLong("user_id", -1)

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.friend_username)
        private val emailTextView: TextView = itemView.findViewById(R.id.friend_email)
        private val addIcon: ImageView = itemView.findViewById(R.id.add_icon)

        fun bind(user: FriendDTO) {
            usernameTextView.text = user.username
            emailTextView.text = user.email

            addIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    addFriend(user.id, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_friend, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            userList
        } else {
            userList.filter { it.username.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    private fun addFriend(friendId: Long, position: Int) {
        val request = AddFriendRequest(userId, friendId)
        friendService.addFriend(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    removeItem(position)
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle the error
            }
        })
    }

    private fun removeItem(position: Int) {
        val userToRemove = filteredList[position]
        filteredList.removeAt(position)
        notifyItemRemoved(position)
        userList.remove(userToRemove)
    }

    fun updateList(newUserList: MutableList<FriendDTO>) {
        userList = newUserList
        filter("")
    }
}
