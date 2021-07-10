package com.example.seesea.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.seesea.ChipSetter
import com.example.seesea.R
import com.example.seesea.databinding.FragmentSearchBinding
import com.example.seesea.roomDB.RoomDataViewModel

class SearchFragment : Fragment() {
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentSearchBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            searchBackButton.setOnClickListener {
                activity?.onBackPressed()
            }
            val divingChipSetter = ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(freeDiving)
                .add(scubaDiving)
                .build()
            val propertyChipSetter = ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(purposeA)
                .add(purposeB)
                .add(purposeC)
                .build()
            val areaChipSetter = ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(north)
                .add(south)
                .add(inside)
                .add(greenIsland)
                .add(lanYu)
                .add(pengHu)
                .add(liuQuiu)
                .build()
            val costChipSetter = ChipSetter(R.color.gray_light_more,R.color.navy_blue)
                .add(costLevel1)
                .add(costLevel2)
                .add(costLevel3)
                .add(costLevel4)
                .build()

            searchClearButton.setOnClickListener {
                freeDiving.isChecked = false
                scubaDiving.isChecked = false

                purposeA.isCheckable = false
                purposeB.isCheckable = false
                purposeC.isCheckable = false

                north.isChecked = false
                inside.isChecked = false
                south.isChecked = false
                greenIsland.isChecked = false
                lanYu.isChecked = false
                pengHu.isChecked = false
                liuQuiu.isChecked = false

                costLevel1.isChecked = false
                costLevel2.isChecked = false
                costLevel3.isChecked = false
                costLevel4.isChecked = false
            }

            val chipArr = mutableListOf<String>()
            val strArr = mutableListOf<MutableList<String>>()
            val intArr = mutableListOf<MutableList<Int>>()
            searchSendButton.setOnClickListener {
                val divingType = divingChipSetter.translate()
                val property = propertyChipSetter.translate()
                val area = areaChipSetter.translate()
                val cost = costChipSetter.translate()

                val d = divingType.getIntArr()
                val p = property.getIntArr()
                val a = area.getIntArr()
                val c = cost.getIntArr()

                strArr.add(divingType.getStrArr())
                strArr.add(property.getStrArr())
                strArr.add(area.getStrArr())
                strArr.add(cost.getStrArr())
                intArr.add(d)
                intArr.add(p)
                intArr.add(a)
                intArr.add(c)

                chipArr.addAll(divingType.getStrArr())
                chipArr.addAll(property.getStrArr())
                chipArr.addAll(area.getStrArr())
                chipArr.addAll(cost.getStrArr())
                viewModel.roomListPage.searchFilter(ChipSetter.intTranslateToStr(d), ChipSetter.intTranslateToStr(p),ChipSetter.intTranslateToStr(a), ChipSetter.intTranslateToStr(c))
                viewModel.roomListPage.setSearchChip(chipArr,strArr,intArr)
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}