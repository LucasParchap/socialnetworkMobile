package com.example.socialnetworkmobile.ui.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.R
import com.example.socialnetworkmobile.model.User

class FriendsAdapter(private var friendsList: MutableList<User>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    private var filteredList: MutableList<User> = friendsList.toMutableList()

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.friend_username)
        private val emailTextView: TextView = itemView.findViewById(R.id.friend_email)
        private val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(user: User) {
            usernameTextView.text = user.username
            emailTextView.text = user.email

            deleteIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeItem(position)
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

    private fun removeItem(position: Int) {
        val userToRemove = filteredList[position]
        filteredList.removeAt(position)
        notifyItemRemoved(position)
        friendsList.remove(userToRemove)
    }

    fun updateList(newFriendsList: MutableList<User>) {
        friendsList = newFriendsList
        filter("")
    }
}