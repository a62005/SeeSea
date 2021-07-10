package com.example.seesea.room.activity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.ItemHostRightsBinding
import com.example.seesea.room.roomList.USER_TYPE_LIST
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.user.GUEST
import com.example.seesea.user.HOST
import com.example.seesea.user.ParticipantUser
import com.example.seesea.user.UserDetailFragment

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class HostRightsAdapter(private val viewModel: RoomDataViewModel, private val isParticipantUser:Boolean, private val activity: FragmentActivity): ListAdapter<ParticipantUser, HostRightsAdapter.ParticipantDataViewHolder>(
    ParticipantDataComparator()) {

    private val userType = viewModel.userPage.userType.value
    class ParticipantDataViewHolder(val binding: ItemHostRightsBinding) : RecyclerView.ViewHolder(binding.root)


    class ParticipantDataComparator: DiffUtil.ItemCallback<ParticipantUser>(){
        override fun areItemsTheSame(oldItem: ParticipantUser, newItem: ParticipantUser): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ParticipantUser, newItem: ParticipantUser): Boolean {
            return oldItem.participantId == newItem.participantId || oldItem.applicantId == newItem.applicantId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHostRightsBinding.inflate(layoutInflater,parent,false)
        return ParticipantDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantDataViewHolder, position: Int) {
        val participant = getItem(position)
        var type = GUEST
        holder.binding.apply {
            holder.itemView.apply {
            }
            when(userType){
                USER_TYPE_LIST[0]->{
                    type = HOST
                    if(isParticipantUser){
                        hostRightsLeaveButton.visibility = View.VISIBLE
                        hostRightsUserName.text = participant.participantName
                        hostRightsUserDescribe.visibility = View.GONE
                        singupTime.visibility = View.GONE
                        hostRightsLeaveButton.setOnClickListener {
                            val transaction = activity.supportFragmentManager.beginTransaction()
                            val fragment = SignupFragment.newInstance(participant.participantId)
                            transaction.replace(R.id.layout_leave,fragment, SignupFragment::class.java.simpleName)
                                .addToBackStack(null)
                                .commit()
                        }
                    }else{

                        viewModel.userPage.participantUser.observe(activity){//當前報名者
                            viewModel.roomPage.currentRoom.observe(activity){room->
                                if(room.participantNumber > it.size+1){
                                    hostRightsAgreeButton.visibility = View.VISIBLE
                                }else{
                                    hostRightsAgreeButton.visibility = View.INVISIBLE
                                }
                            }
                        }
                        hostRightsUserName.text = participant.applicantName
                        hostRightsUserDescribe.text = participant.applicatingDescription
                        hostRightsAgreeButton.setOnClickListener {
                            viewModel.userPage.agreeSignUp(participant.applicantId)
                            viewModel.userPage.getParticipantUser()
                            notifyDataSetChanged()
                        }
                    }
                }
                USER_TYPE_LIST[1]->{
                    if(getItem(position).participantId == viewModel.userId){
                        hostRightsLeaveButton.visibility = View.VISIBLE
                    }
                    hostRightsUserName.text = participant.participantName
                    singupTime.visibility = View.GONE
                    signDateTime.visibility = View.GONE
                    hostRightsUserDescribe.visibility = View.GONE
                    hostRightsLeaveButton.setOnClickListener {
                        val transaction = activity.supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.layout_leave, SignupFragment(), SignupFragment::class.java.simpleName)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }

            if(isParticipantUser){
                Glide.with(holder.itemView.context).load(getItem(position).participantImage).placeholder(R.drawable.activity_host_image).into(userImage)
            }else{
                Glide.with(holder.itemView.context).load(getItem(position).applicantImage).placeholder(R.drawable.activity_host_image).into(userImage)
                signDateTime.text = getItem(position).applicatingDateTime

            }
            userImage.setOnClickListener {
                val userDetailFragment = UserDetailFragment.newInstance(type)
                if(isParticipantUser) viewModel.roomPage.getHostData(participant.participantId) else viewModel.roomPage.getHostData(participant.applicantId)
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_userDetail, userDetailFragment, UserDetailFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }



        }
    }
}