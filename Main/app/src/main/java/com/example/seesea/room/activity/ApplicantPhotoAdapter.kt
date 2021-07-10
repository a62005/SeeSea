package com.example.seesea.room.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seesea.R
import com.example.seesea.databinding.ItemApplicantPhotoBinding
import com.example.seesea.search.SearchAdapter

class ApplicantPhotoAdapter: ListAdapter<String, ApplicantPhotoAdapter.PhotoViewHolder>(SearchAdapter.SearchComparator()) {

    class PhotoViewHolder(val binding: ItemApplicantPhotoBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemApplicantPhotoBinding.inflate(layoutInflater,parent,false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.apply {
            Glide.with(itemView.context).load(getItem(position)).placeholder(R.drawable.activity_host_image).into(binding.applicantPhoto)
        }
    }
}