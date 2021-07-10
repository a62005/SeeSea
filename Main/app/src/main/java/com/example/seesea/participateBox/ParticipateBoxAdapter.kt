package com.example.seesea.participateBox

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seesea.R
import com.example.seesea.databinding.ItemParticipateBoxBinding
import com.example.seesea.room.PBoxRoom
import com.example.seesea.room.activity.RoomFragment
import com.example.seesea.room.roomList.HOLDING
import com.example.seesea.roomDB.RoomDataViewModel

class ParticipateBoxAdapter(private val viewModel: RoomDataViewModel,private val activity:FragmentActivity,private val isMap:Boolean): ListAdapter<PBoxRoom, ParticipateBoxAdapter.ParticipateBoxViewHolder>(
    ParticipateBoxComparator()) {

    private val statusArr = activity.resources.getStringArray(R.array.activityStatus)
    private val red = activity.resources.getColor(R.color.salmon_pink,null)

    class ParticipateBoxViewHolder(val binding: ItemParticipateBoxBinding): RecyclerView.ViewHolder(binding.root)
    class ParticipateBoxComparator:DiffUtil.ItemCallback<PBoxRoom>(){
        override fun areItemsTheSame(oldItem: PBoxRoom, newItem: PBoxRoom): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PBoxRoom, newItem: PBoxRoom): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ParticipateBoxViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParticipateBoxBinding.inflate(layoutInflater,parent,false)
        return ParticipateBoxViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipateBoxViewHolder, position: Int) {
        val room = getItem(position)
        holder.binding.apply {

            textViewPBoxRoomName.text = room.activityName
            textViewPBoxDivingType.text = room.divingType
            textViewPBoxDivingPlace.text = room.activityPlace
            textViewPBoxRoomStatus.text = statusArr[room.activityStatusCode]

            if(room.activityStatusCode == HOLDING){
                textViewPBoxRoomStatus.setTextColor(red)
            }
            if(isMap){
                textViewPBoxUserStatus.visibility = View.GONE
            }

            holder.itemView.apply {
                setOnClickListener {
                    viewModel.roomPage.getRoomById(room.activityId)
                    val transaction = activity?.supportFragmentManager!!.beginTransaction()
                    transaction.replace(R.id.layout_main, RoomFragment(), RoomFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
                Log.d("test","${viewModel.userId}  ${room.hostId}")
                if(viewModel.userId == room.hostId){
                    textViewPBoxUserStatus.text = resources.getString(R.string.participate_userHost)
                }else{
                    textViewPBoxUserStatus.text = resources.getString(R.string.participate_userParticipate)
                }

            }
            viewModel.user.observe(activity){user->
                for(i in user.userSigningUpActivity){
                    if(room.activityId == i){
                        itemPBox.setBackgroundResource(R.drawable.shape_pbox_signingup)
                    }
                }
                for(i in user.userFinishActivity){
                    if(room.activityId == i){
                        itemPBox.setBackgroundResource(R.drawable.shape_pbox_ending)
                    }
                }

            }
        }
    }
}