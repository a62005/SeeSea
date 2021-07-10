package com.example.seesea.participateBox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentParticipateBoxBinding
import com.example.seesea.roomDB.RoomDataViewModel

class ParticipateBoxFragment : Fragment() {
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentParticipateBoxBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_participate_box, container, false)
        binding = FragmentParticipateBoxBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pBoxPage.loadUserRoomList()
        binding?.apply {
            val participatingAdapter = ParticipateBoxAdapter(viewModel,activity as FragmentActivity,false)
            val signingUpAdapter = ParticipateBoxAdapter(viewModel,activity as FragmentActivity,false)
            val finishedAdapter = ParticipateBoxAdapter(viewModel,activity as FragmentActivity,false)
            recycleViewParticipateParticipating.adapter = participatingAdapter
            recycleViewParticipateSigningUp.adapter = signingUpAdapter
            recycleViewParticipateFinish.adapter = finishedAdapter
            viewModel.roomListPage.participatingRoom.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    textViewParticipateParticipatingNoData.visibility = View.VISIBLE
                    return@observe
                }
                textViewParticipateParticipatingNoData.visibility = View.INVISIBLE
                participatingAdapter.submitList(it)
            }

            viewModel.roomListPage.signingUpRoom.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    textViewParticipateSigningUpNoData.visibility = View.VISIBLE
                    return@observe
                }
                textViewParticipateSigningUpNoData.visibility = View.INVISIBLE
                signingUpAdapter.submitList(it)
            }

            viewModel.roomListPage.finishedRoom.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    textViewParticipateFinishNoData.visibility = View.VISIBLE
                    return@observe
                }
                textViewParticipateFinishNoData.visibility = View.INVISIBLE
                finishedAdapter.submitList(it)
            }
            pBoxBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}