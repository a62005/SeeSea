package com.example.seesea.room.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentFavoriteBinding
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.room.roomList.RoomListAdapter


class FavoriteFragment : Fragment() {

    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentFavoriteBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        binding = FragmentFavoriteBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.roomListPage.getFavoriteRoomList()
        binding?.apply {
            activity.apply {
                val favoriteAdapter = RoomListAdapter(this,viewLifecycleOwner,this!!.application,viewModel)
                recyclerViewFavorite.adapter = favoriteAdapter
                viewModel.roomListPage.favoriteRoom.observe(viewLifecycleOwner){
                    if(it.isEmpty()){
                        mainHint.visibility = View.VISIBLE
                    }else{
                        mainHint.visibility = View.INVISIBLE
                    }
                    favoriteAdapter.submitList(it)
                }
            }

        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}