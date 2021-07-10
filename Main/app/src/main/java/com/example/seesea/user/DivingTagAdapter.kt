package com.example.seesea.user

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seesea.R
import com.example.seesea.databinding.ItemDivingTagBinding
import com.example.seesea.roomDB.RoomDataViewModel


class DivingTagAdapter (private val viewModel: RoomDataViewModel,private val isModify:Boolean,private val isLastTag:Boolean): ListAdapter<String, DivingTagAdapter.DivingTagViewHolder>(DivingTagComparator()) {

    class DivingTagViewHolder(val binding: ItemDivingTagBinding): RecyclerView.ViewHolder(binding.root)

    class DivingTagComparator: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.chars() === newItem.chars()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivingTagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDivingTagBinding.inflate(layoutInflater,parent,false)
        return DivingTagViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: DivingTagViewHolder, position: Int) {
        holder.binding.apply {
            textViewUserDivingTag.text = getItem(position)
            if(isModify){
                userDivingTagCancel.visibility = View.VISIBLE
                if(isLastTag){
                    userDivingTagCancel.setImageResource(R.drawable.create_room)
                }
            }
            userDivingTagCancel.setOnClickListener {
                if(isModify){
                    if(isLastTag){
                        viewModel.userPage.addUserTag(getItem(position))
                        return@setOnClickListener
                    }
                    viewModel.userPage.removeUserTag(getItem(position))
                }
            }
        }
    }
}