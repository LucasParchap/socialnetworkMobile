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
import com.example.socialnetworkmobile.service.FriendsService
import com.example.socialnetworkmobile.service.NotificationsService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFriendFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var addFriendAdapter: AddFriendAdapter
    private val friendsViewModel: FriendsViewModel by viewModel()
    private val friendsService: FriendsService by inject()
    private val notificationsService: NotificationsService by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.search_view_add_friend)
        recyclerView = view.findViewById(R.id.recycler_view_add_friend)

        addFriendAdapter = AddFriendAdapter(requireContext(), mutableListOf(), friendsService, notificationsService)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = addFriendAdapter

        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getLong("user_id", -1) ?: -1
        if (userId != -1L) {
            friendsViewModel.fetchNonFriends(userId)
        }

        friendsViewModel.nonFriendsList.observe(viewLifecycleOwner, { nonFriends ->
            addFriendAdapter.updateList(nonFriends.toMutableList())
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                addFriendAdapter.filter(newText ?: "")
                return true
            }
        })
    }
}
