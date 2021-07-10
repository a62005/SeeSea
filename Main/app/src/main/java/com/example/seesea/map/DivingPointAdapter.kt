package com.example.seesea.map

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.seesea.R
import com.example.seesea.databinding.ItemDivingPointBinding
import com.example.seesea.roomDB.DivingPoint
import com.example.seesea.roomDB.RoomDataViewModel

//Maps下方的潛點資訊item
class DivingPointAdapter(private val viewModel: RoomDataViewModel,private val activity:FragmentActivity): ListAdapter<DivingPoint, DivingPointAdapter.DivingPointViewHolder>(DivingPointComparator()){

    private val glide = RequestOptions().placeholder(R.drawable.mainlist_on_click).skipMemoryCache(true)
    private var isScrolling = false

    class DivingPointViewHolder(val binding:ItemDivingPointBinding):RecyclerView.ViewHolder(binding.root)
    class DivingPointComparator: DiffUtil.ItemCallback<DivingPoint>(){
        override fun areItemsTheSame(oldItem: DivingPoint, newItem: DivingPoint): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DivingPoint, newItem: DivingPoint): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivingPointViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDivingPointBinding.inflate(layoutInflater,parent,false)
        return DivingPointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DivingPointViewHolder, position: Int) {
        val dp = getItem(position)
        holder.binding.apply {
            holder.itemView.apply {
                setOnClickListener {
                    viewModel.mapPage.setOnClickMarkerNumber(dp.divingPointName)
                }
                if(!isScrolling) Glide.with(context).load(dp.divingPointPicture).apply(glide).into(dpImage)
            }
            dpArea.text = dp.areaTag
            dpPlace.text = dp.divingPointName
            dpDivingType.text = dp.divingTypeTag.toString().replace('[',' ').replace(']',' ')
            dpDifficulty.text = dp.divingDifficulty
            hostActivityNumber.text = dp.divingPointActivityNumber.toString()

            hostActivity.setOnClickListener {
                if(dp.divingPointActivityNumber == 0)return@setOnClickListener
                viewModel.mapPage.getDivingPointActivity(dp.divingPointId)
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_holdingActivity, DivingPointActivityFragment(), DivingPointActivityFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    fun setScrolling(boolean: Boolean){
        isScrolling = boolean
    }

}