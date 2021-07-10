package com.example.seesea.map.mapSearch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seesea.databinding.ItemMapSearchBinding
import com.example.seesea.roomDB.DivingPoint
import com.example.seesea.roomDB.RoomDataViewModel

//搜尋的地點顯示列
class MapSearchAdapter(private val viewModel: RoomDataViewModel,private val recyclerView: RecyclerView,private val editText: EditText): ListAdapter<DivingPoint, MapSearchAdapter.MapSearchViewHolder>(MapSearchComparator()) {

    class MapSearchViewHolder(val binding: ItemMapSearchBinding):RecyclerView.ViewHolder(binding.root)

    class MapSearchComparator:DiffUtil.ItemCallback<DivingPoint>(){
        override fun areItemsTheSame(oldItem: DivingPoint, newItem: DivingPoint): Boolean {
            return oldItem.divingPointName.chars() === newItem.divingPointName.chars()
        }

        override fun areContentsTheSame(oldItem: DivingPoint, newItem: DivingPoint): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapSearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMapSearchBinding.inflate(layoutInflater,parent,false)
        return MapSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapSearchViewHolder, position: Int) {
        holder.binding.apply {
            pointName.text = getItem(position).divingPointName
        }
        holder.itemView.apply {
            setOnClickListener {
                viewModel.mapPage.setSinglePoint(getItem(position))
                recyclerView.visibility = View.GONE
                editText.text.clear()
            }
        }
    }


}