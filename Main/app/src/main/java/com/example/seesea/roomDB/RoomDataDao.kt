package com.example.seesea.roomDB

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RoomDataDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun add(vararg divingPoint: DivingPoint)
//
//    @Query("SELECT * FROM divingPoint_table ORDER BY divingPointId" )
//    fun getAll(): List<DivingPoint>
//
//    @Query("SELECT divingPointName FROM divingPoint_table WHERE areaTag LIKE:areaTag")
//    fun getDivingPlaceByArea(areaTag :String):List<String>

    @Query("DELETE FROM divingPoint_table")
    suspend fun deleteAll()
}