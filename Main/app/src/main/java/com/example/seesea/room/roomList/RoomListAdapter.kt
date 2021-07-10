package com.example.seesea.room.roomList

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.R.color.*
import com.example.seesea.databinding.ItemRoomBinding
import com.example.seesea.room.ActivityRoom
import com.example.seesea.room.activity.*
import com.example.seesea.roomDB.RoomDataViewModel


class RoomListAdapter(private val activity:FragmentActivity?, val lifecycleOwner: LifecycleOwner, val application: Application, private val viewModel: RoomDataViewModel): ListAdapter<ActivityRoom, RoomListAdapter.RoomListViewHolder>(
    RoomComparator()) {

    private val statusArr = activity!!.resources.getStringArray(R.array.activityStatus)
    private val red = activity!!.resources.getColor(salmon_pink,null)
    private val gray = activity!!.resources.getColor(gray_light_more,null)

    class RoomListViewHolder(val binding: ItemRoomBinding): RecyclerView.ViewHolder(binding.root)

    class RoomComparator: DiffUtil.ItemCallback<ActivityRoom>(){
        override fun areItemsTheSame(oldItem: ActivityRoom, newItem: ActivityRoom): Boolean {
            return newItem === oldItem
        }

        override fun areContentsTheSame(oldItem: ActivityRoom, newItem: ActivityRoom): Boolean {
            return oldItem.isFavorite == newItem.isFavorite
                    || oldItem.favoriteNumber == newItem.favoriteNumber
                    || oldItem.messageBoard.size == newItem.messageBoard.size
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRoomBinding.inflate(layoutInflater,parent,false)
        return RoomListViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: RoomListViewHolder, position: Int) {

        val room = getItem(position)
        holder.binding.apply {
            textViewHostName.text = room.hostName
            textViewListRoomName.text = room.activityName
            textViewListRoomDateTime.text = room.activityDate
            textViewListDivingType.text = room.divingType
            textViewListArea.text = room.activityArea
            textViewListPlace.text = room.activityPlace
            textViewListStatus.text = statusArr[room.activityStatusCode]
            messageNumber.text = room.messageBoard.size.toString()
            favoriteNumber.text = room.favoriteNumber.toString()

            holder.itemView.apply {
                when(room.activityStatusCode){
                1->textViewListStatus.setTextColor(red)
                else->textViewListStatus.setTextColor(gray)
                }
                setOnClickListener{
                    viewModel.roomPage.setCurrentRoom(room)
                    val transaction = activity?.supportFragmentManager!!.beginTransaction()
                    transaction.replace(R.id.layout_main, RoomFragment(), RoomFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
            }

            Glide.with(application).load(room.hostImage).placeholder(R.drawable.activity_host_image).into(hostImage)
            viewModel.user.observe(lifecycleOwner){ user->
                if(user.userFavoriteRoom.contains(room.activityId)) {
                    imageViewListFavorite.setImageResource(R.drawable.list_heart_onclick)
                    room.isFavorite = true
                }else{
                    imageViewListFavorite.setImageResource(R.drawable.activity_heart_unclick)
                    room.isFavorite = false
                }
            }

            imageViewListFavorite.setOnClickListener {
                if(room.isFavorite){
                    imageViewListFavorite.setImageResource(R.drawable.activity_heart_unclick)
                    viewModel.roomListPage.deleteFavorite(room.activityId,room)
                    room.favoriteNumber -= 1
                }else{
                    imageViewListFavorite.setImageResource(R.drawable.list_heart_onclick)
                    viewModel.roomListPage.addFavorite(room.activityId,room)
                    room.favoriteNumber += 1
                }
                room.isFavorite = !room.isFavorite
                notifyItemChanged(position)
            }
        }

    }

}