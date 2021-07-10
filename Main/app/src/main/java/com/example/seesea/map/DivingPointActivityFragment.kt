package com.example.seesea.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentDivingPointActivityBinding
import com.example.seesea.participateBox.ParticipateBoxAdapter
import com.example.seesea.roomDB.RoomDataViewModel

//當前潛點舉辦的活動頁面
class DivingPointActivityFragment : Fragment() {

    private var binding : FragmentDivingPointActivityBinding? = null
    private val viewModel: RoomDataViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_diving_point_activity, container, false)
        binding = FragmentDivingPointActivityBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            val activityAdapter = ParticipateBoxAdapter(viewModel,activity as FragmentActivity,true)
            recyclerViewDivingPointActivity.adapter = activityAdapter
            viewModel.mapPage.holdingActivity.observe(viewLifecycleOwner){
                activityAdapter.submitList(it)
            }
            back.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}