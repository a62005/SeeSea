package com.example.seesea.search


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seesea.databinding.ItemSearchBinding
import com.example.seesea.roomDB.RoomDataViewModel


class SearchAdapter(private val viewModel: RoomDataViewModel): ListAdapter<String, SearchAdapter.SearchViewHolder>(SearchComparator()) {

    class SearchViewHolder(val binding:ItemSearchBinding):RecyclerView.ViewHolder(binding.root)

    class SearchComparator: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.chars() === newItem.chars()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchBinding.inflate(layoutInflater,parent,false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.apply {
            itemSearchName.text = getItem(position)
            itemSearchCancel.setOnClickListener {
                viewModel.roomListPage.cancelSearchChip(getItem(position))
            }
        }
    }
}