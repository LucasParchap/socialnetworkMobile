package com.example.socialnetworkmobile.ui.friends

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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

class PendingRequestsAdapter(
    private val context: Context,
    private var pendingRequestsList: MutableList<FriendDTO>,
    private val friendsService: FriendsService
) : RecyclerView.Adapter<PendingRequestsAdapter.UserViewHolder>(), Filterable {

    private var filteredList: MutableList<FriendDTO> = pendingRequestsList.toMutableList()
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val userId = sharedPreferences.getLong("user_id", -1)

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.friend_username)
        private val emailTextView: TextView = itemView.findViewById(R.id.friend_email)
        private val hourglassIcon: ImageView = itemView.findViewById(R.id.hourglass_icon)
        private val crossIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(user: FriendDTO) {
            usernameTextView.text = user.username
            emailTextView.text = user.email

            hourglassIcon.visibility = View.VISIBLE
            hourglassIcon.isClickable = false

            crossIcon.setOnClickListener {
                val request = RemoveFriendRequest(userId, user.id)
                friendsService.removeFriend(request).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            removeItem(adapterPosition)
                        } else {
                            Log.e("PendingRequestsAdapter", "Error removing friend")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("PendingRequestsAdapter", "Error removing friend", t)
                    }
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pending_requests, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updateList(newList: List<FriendDTO>) {
        pendingRequestsList = newList.toMutableList()
        filter.filter("")
    }

    private fun removeItem(position: Int) {
        filteredList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredList = if (charString.isEmpty()) {
                    pendingRequestsList
                } else {
                    pendingRequestsList.filter {
                        it.username.contains(charString, ignoreCase = true) ||
                                it.email.contains(charString, ignoreCase = true)
                    }.toMutableList()
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<FriendDTO>
                notifyDataSetChanged()
            }
        }
    }
}