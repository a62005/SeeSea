package com.example.seesea.room.activity


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.ItemMessageBinding
import com.example.seesea.room.Message
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.user.GUEST
import com.example.seesea.user.HOST
import com.example.seesea.user.UserDetailFragment
import java.util.*

class MessageAdapter(private val viewModel: RoomDataViewModel,val activity: FragmentActivity):ListAdapter<Message,MessageAdapter.MessageViewHolder>(MessageComparator()) {

    private var isOpen = false

    class MessageViewHolder(val binding:ItemMessageBinding):RecyclerView.ViewHolder(binding.root)
    class MessageComparator:DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem === newItem
//            return oldItem.message.chars() === newItem.message.chars() && oldItem.messageDateTime === newItem.messageDateTime
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(layoutInflater,parent,false)
        return MessageViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val user = getItem(position)
        var type = GUEST
        var currentTime = Date(System.currentTimeMillis())
        holder.binding.apply {
            Glide.with(activity).load(user.userImage).placeholder(R.drawable.activity_host_image).into(itemMessageUserImage)
            itemMessageUserNickName.text = user.userName
            itemMessageDate.text = user.messageDateTime
            itemMessageMessage.text = user.message
            deleteFrame.x = 350F
            deleteFrame.y = 50F
            if(user.userId == viewModel.userId) type = HOST
            val userDetailFragment = UserDetailFragment.newInstance(type)
            holder.itemView.apply {
                itemMessageUserNickName.setOnClickListener {
                    viewModel.roomPage.getHostData(user.userId)
                    val transaction = activity.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.layout_userDetail, userDetailFragment, UserDetailFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
                itemMessageUserImage.setOnClickListener {
                    viewModel.roomPage.getHostData(user.userId)
                    val transaction = activity.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.layout_userDetail, userDetailFragment, UserDetailFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                setOnLongClickListener {
                    setOnTouchListener { _, event ->
                        if(event.action == MotionEvent.ACTION_DOWN && !isOpen){
                            deleteFrame.x = 0F
                            deleteFrame.y = 0F
                            deleteFrame.x = event.x
                            deleteFrame.y = event.y - 50.0F
                            if(deleteFrame.x >800) deleteFrame.x = 800F
                            if(deleteFrame.y <0 )deleteFrame.y = 0F
                        }
                        true
                    }
                    if(user.userId == viewModel.userId){
                        deleteFrame.visibility = View.VISIBLE
                    }
                    true
                }
                setOnClickListener {
                    deleteFrame.visibility = View.INVISIBLE

                }
                deleteFrame.setOnClickListener {
                    viewModel.roomPage.deleteMessage(position,user.messageId)
                }
            }
        }

    }
}