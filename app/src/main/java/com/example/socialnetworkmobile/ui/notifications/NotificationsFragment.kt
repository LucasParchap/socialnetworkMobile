package com.example.socialnetworkmobile.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworkmobile.databinding.FragmentNotificationsBinding
import com.example.socialnetworkmobile.service.FriendsService
import com.example.socialnetworkmobile.service.NotificationsService
import com.example.socialnetworkmobile.service.UserService
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val notificationsViewModel: NotificationsViewModel by viewModel()
    private lateinit var notificationsAdapter: NotificationsAdapter
    private val notificationsService: NotificationsService by inject()
    private val friendsService: FriendsService by inject()
    private val userService: UserService by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(context)
        notificationsViewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            notificationsAdapter = NotificationsAdapter(notifications.toMutableList(), notificationsService, friendsService, userService)
            binding.recyclerViewNotifications.adapter = notificationsAdapter

            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    notificationsAdapter.removeNotification(position)
                }
            })
            itemTouchHelper.attachToRecyclerView(binding.recyclerViewNotifications)
        }

        val sharedPreferences: SharedPreferences? = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getLong("user_id", -1) ?: -1

        if (userId != -1L) {
            notificationsViewModel.loadNotifications(userId)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
