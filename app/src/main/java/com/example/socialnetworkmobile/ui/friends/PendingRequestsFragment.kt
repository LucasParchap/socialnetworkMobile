package com.example.socialnetworkmobile.ui.friends

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.R
import com.example.socialnetworkmobile.model.friends.FriendDTO
import com.example.socialnetworkmobile.service.FriendsService
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingRequestsFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var pendingRequestsAdapter: PendingRequestsAdapter
    private val friendsService: FriendsService by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchViewPendingRequests)
        recyclerView = view.findViewById(R.id.recyclerViewPendingRequests)

        pendingRequestsAdapter = PendingRequestsAdapter(requireContext(), mutableListOf(), friendsService)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = pendingRequestsAdapter

        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getLong("user_id", -1) ?: -1
        if (userId != -1L) {
            loadPendingRequests(userId)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pendingRequestsAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun loadPendingRequests(userId: Long) {
        friendsService.getPendingFriendRequests(userId).enqueue(object : Callback<List<FriendDTO>> {
            override fun onResponse(call: Call<List<FriendDTO>>, response: Response<List<FriendDTO>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        pendingRequestsAdapter.updateList(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<FriendDTO>>, t: Throwable) {
                // Handle failure
            }
        })
    }
}