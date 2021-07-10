package com.example.seesea.room.recommend

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.ItemRecommendBinding
import com.example.seesea.room.ActivityRoom
import com.example.seesea.room.activity.RoomFragment
import com.example.seesea.room.roomList.RoomListAdapter
import com.example.seesea.roomDB.RoomDataViewModel

class RecommendAdapter(private val viewModel: RoomDataViewModel,val activity:FragmentActivity): ListAdapter<ActivityRoom, RecommendAdapter.RecommendViewHolder>(RoomListAdapter.RoomComparator()) {

    private val statusArr = activity.resources.getStringArray(R.array.activityStatus)

    class RecommendViewHolder(val binding:ItemRecommendBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): RecommendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecommendBinding.inflate(layoutInflater,parent,false)
        return RecommendViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        val room = getItem(position)
        holder.binding.apply {
            Glide.with(activity.application).load(room.hostImage).placeholder(R.drawable.mainlist_on_click).into(recommendImage)
            recommendRoomName.text = room.activityName
            recommendDate.text = room.activityDate
            recommendDivingType.text = room.divingType
            recommendExperience.text
            recommendArea.text = room.activityArea
            recommendPlace.text = room.activityPlace
            activityParticipantNumber.text = "${room.participantNumber} 人團"
            messageNumber.text = room.messageBoard.size.toString()
            favoriteNumber.text = room.favoriteNumber.toString()

            holder.itemView.apply {
                setOnClickListener{
                    viewModel.roomPage.setCurrentRoom(room)
                    val transaction = activity.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.layout_main, RoomFragment(), RoomFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}