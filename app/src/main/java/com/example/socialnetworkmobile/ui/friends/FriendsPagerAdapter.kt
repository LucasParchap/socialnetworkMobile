package com.example.socialnetworkmobile.ui.friends

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FriendsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendsListFragment()
            1 -> PendingRequestsFragment()
            2 -> AddFriendFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}