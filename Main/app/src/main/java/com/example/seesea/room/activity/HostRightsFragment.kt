package com.example.seesea.room.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.seesea.R
import com.example.seesea.databinding.FragmentHostRightsBinding
import com.example.seesea.room.roomList.*
import com.example.seesea.roomDB.RoomDataViewModel

class HostRightsFragment : Fragment() {
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentHostRightsBinding? = null
    private var isOverTime = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_host_rights, container, false)
        binding = FragmentHostRightsBinding.bind(view)
        return view
    }
    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userPage.getParticipantUser()
        binding?.apply {
            viewModel.roomPage.currentRoom.observe(viewLifecycleOwner){room->
                if(room.activityStatusCode == DECLARE_FULL){
                    declareFull.visibility = View.GONE
                }else if(room.activityStatusCode == ENDING || room.activityStatusCode == FAIL){
                    isOverTime = true
                    declareFull.setBackgroundColor(Color.parseColor("#7F8388"))
                }
                textViewHosRightsRoomName.text = room.activityName
                viewModel.user.observe(viewLifecycleOwner){user->
                    if(room.hostId != user.userId){
                        dodgeCanvas.visibility = View.VISIBLE
                        recyclerViewSigningUp.visibility = View.GONE
                        declareFull.visibility = View.GONE
                    }
                }
                maxNumber.text = room.participantNumber.toString()
            }
            val participantUserAdapter = HostRightsAdapter(viewModel,true,activity as FragmentActivity)
            val signUpUserAdapter = HostRightsAdapter(viewModel,false,activity as FragmentActivity)
            recyclerViewParticipant.adapter = participantUserAdapter
            recyclerViewSigningUp.adapter = signUpUserAdapter

            viewModel.userPage.participantUser.observe(viewLifecycleOwner){
                if(it.isEmpty()){
                    declareFull.setBackgroundColor(Color.parseColor("#7F8388"))
                }else{
                    declareFull.setBackgroundColor(Color.parseColor("#42a5f5"))
                }
                participantUserAdapter.submitList(it)
                participantNumber.text = (it.size+1).toString()
                participantUserAdapter.notifyItemChanged(it.size)
                signUpUserAdapter.notifyDataSetChanged()
            }
            viewModel.userPage.signUpUser.observe(viewLifecycleOwner){
                signUpUserAdapter.submitList(it)
                applicantNumber.text = it.size.toString()
                signUpUserAdapter.notifyDataSetChanged()
            }
            imageViewHostRightsBack.setOnClickListener {
                activity?.onBackPressed()
            }
            declareFull.setOnClickListener {
                if(viewModel.userPage.participantUser.value!!.isEmpty() || isOverTime){
                    return@setOnClickListener
                }
                layoutDeclareFullHint.visibility = View.VISIBLE
            }
            buttonCancelDeclare.setOnClickListener {
                layoutDeclareFullHint.visibility = View.INVISIBLE
            }
            buttonConfirmDeclare.setOnClickListener {
                viewModel.roomPage.declareFull()
                layoutDeclareFullHint.visibility = View.INVISIBLE
                declareFull.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}