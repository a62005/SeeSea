package com.example.seesea

import android.annotation.SuppressLint
import com.google.android.material.chip.Chip

class ChipSetter(private val beforeColor: Int, private val afterColor:Int) {

    private val chipArr = mutableListOf<Chip>()

    private var count = 1
    private val intArr = mutableListOf<Int>()
    private val strArr = mutableListOf<String>()

    fun add(chip: Chip):ChipSetter{
        chipArr.add(chip)
        return this
    }

    fun build():ChipSetter{
        chipArr.forEach { it.setOnCheckedChangeListener { _, isChecked -> onCheckedChanged(isChecked,it)  } }
        return this
    }

    @SuppressLint("ResourceAsColor")
    private fun onCheckedChanged(isChecked:Boolean, chip: Chip){
        if(isChecked){
            chip.setChipBackgroundColorResource(afterColor)
        }else{
            chip.setChipBackgroundColorResource(beforeColor)
        }
    }

    fun translate(): ChipSetter {
        chipArr.forEach {
            if (it.isChecked) {
                intArr.add(count)
                strArr.add("${it.text}")
            }
            count++
        }
        return this
    }
    fun getIntArr()=intArr
    fun getStrArr()=strArr

    companion object{
        fun intTranslateToStr(tmpIntArr:List<Int>):String{
            var str = ""
            for(i in tmpIntArr.indices){
                if(i == 0){
                    str += "${tmpIntArr[i]}"
                    continue
                }
                str += ",${tmpIntArr[i]}"
            }
            return str
        }
    }

}