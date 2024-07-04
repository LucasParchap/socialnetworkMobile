package com.example.socialnetworkmobile.ui.friends

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.R
import com.example.socialnetworkmobile.model.friends.FriendDTO
import com.example.socialnetworkmobile.model.friends.AddFriendRequest
import com.example.socialnetworkmobile.model.friends.RemoveFriendRequest
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
        private val crossIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(user: FriendDTO) {
            usernameTextView.text = user.username
            emailTextView.text = user.email
            checkFriendStatus(user, addIcon, crossIcon, adapterPosition)
        }

        private fun checkFriendStatus(user: FriendDTO, addIcon: ImageView, crossIcon: ImageView, position: Int) {
            friendService.areBothFriends(userId, user.id).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful && response.body() == true) {
                        removeItem(position)
                    } else {
                        checkIfUserAddedFriend(user, addIcon, crossIcon)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("AddFriendAdapter", "Error checking if both are friends", t)
                }
            })
        }

        private fun checkIfUserAddedFriend(user: FriendDTO, addIcon: ImageView, crossIcon: ImageView) {
            friendService.hasUserAddedFriend(userId, user.id).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful && response.body() == true) {
                        addIcon.setImageResource(R.drawable.baseline_hourglass_empty_24)
                        addIcon.isClickable = false
                        crossIcon.visibility = View.VISIBLE
                        crossIcon.setOnClickListener {
                            removeFriend(user, addIcon, crossIcon)
                        }
                    } else {
                        addIcon.setImageResource(R.drawable.baseline_person_add_24)
                        addIcon.isClickable = true
                        crossIcon.visibility = View.GONE
                        addIcon.setOnClickListener {
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                addFriend(user, position)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("AddFriendAdapter", "Error checking if user added friend", t)
                }
            })
        }

        private fun addFriend(friend: FriendDTO, position: Int) {
            val request = AddFriendRequest(userId, friend.id)
            friendService.addFriend(request).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        checkFriendStatus(friend, addIcon, crossIcon, position)
                    } else {
                        // Handle the error
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // Handle the error
                }
            })
        }

        private fun removeFriend(friend: FriendDTO, addIcon: ImageView, crossIcon: ImageView) {
            val request = RemoveFriendRequest(userId, friend.id)
            friendService.removeFriend(request).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        addIcon.setImageResource(R.drawable.baseline_person_add_24)
                        addIcon.isClickable = true
                        crossIcon.visibility = View.GONE
                        addIcon.setOnClickListener {
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                addFriend(friend, position)
                            }
                        }
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
            if (position != RecyclerView.NO_POSITION) {
                val userToRemove = filteredList[position]
                filteredList.removeAt(position)
                notifyItemRemoved(position)
                userList.remove(userToRemove)
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

    fun updateList(newUserList: MutableList<FriendDTO>) {
        userList = newUserList
        filter("")
    }
}
