package com.example.seesea.map.mapSearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.seesea.ChipSetter
import com.example.seesea.R
import com.example.seesea.databinding.FragmentMapSearchBinding
import com.example.seesea.roomDB.RoomDataViewModel

class MapSearchFragment : Fragment() {

    private var binding : FragmentMapSearchBinding? = null
    private val viewModel: RoomDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_search, container, false)
        binding = FragmentMapSearchBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            searchBackButton.setOnClickListener {
                activity?.onBackPressed()
            }

            val areaChipSetter = ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(north).add(south).add(inside).add(greenIsland).add(lanYu).add(pengHu).add(liuQuiu)
                .build()

            val typeChipSetter =  ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(freeDiving).add(scubaDiving)
                .build()

            val levelChipSetter = ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(element).add(junior).add(senior)
                .build()

            searchSendButton.setOnClickListener {
                val area = areaChipSetter.translate().getStrArr()
                val type = typeChipSetter.translate().getStrArr()
                val level= levelChipSetter.translate().getStrArr()
                if(area.isEmpty())area.add("")
                if(type.isEmpty())type.add("")
                if(level.isEmpty())level.add("")
                viewModel.mapPage.mapSearchByChip(area[0],type[0],level[0])
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}