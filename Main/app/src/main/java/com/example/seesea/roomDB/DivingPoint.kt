package com.example.seesea.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey


data class DivingPoint(
    var searchId:Int,
    val divingPointId:Int,
    val divingPointName: String,
    val longitude:Double,
    val latitude:Double,
    val divingPlaceDescription:String,
    val areaTag:String,
    val divingTypeTag:List<String>,
    val divingDifficulty:String,
    val divingPointPicture:String,
    val divingPointActivityNumber:Int,
)

@Entity(tableName = "divingPoint_table")
data class Test(
    @PrimaryKey
    val id:Int = 0,
    val testId:Int,
)