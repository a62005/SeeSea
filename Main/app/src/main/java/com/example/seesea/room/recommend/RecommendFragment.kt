package com.example.seesea.room.recommend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentRecommendBinding
import com.example.seesea.roomDB.RoomDataViewModel


class RecommendFragment : Fragment() {
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentRecommendBinding? = null
    private var isFirstTimeNoRecommend = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recommend, container, false)
        binding = FragmentRecommendBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.roomListPage.getRecommendRoom()
        binding?.apply {
            val recommendAdapter = RecommendAdapter(viewModel,activity as FragmentActivity)
            recyclerViewRecommend.adapter = recommendAdapter
            viewModel.roomListPage.recommendRoom.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    mainHint.visibility = View.VISIBLE
                    isFirstTimeNoRecommend = true
                    viewModel.roomListPage.setOneRecommendRoom()
                    return@observe
                }
                mainHint.visibility = View.INVISIBLE
                if(isFirstTimeNoRecommend)mainHint.visibility = View.VISIBLE
                recommendAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}