package com.example.seesea.room

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.seesea.room.favorite.FavoriteFragment
import com.example.seesea.room.recommend.RecommendFragment
import com.example.seesea.room.roomList.RoomListFragment

class RoomManagerFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragments = arrayListOf(RoomListFragment(), RecommendFragment(), FavoriteFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}