package com.example.seesea.roomDB

import androidx.annotation.WorkerThread

class RoomDataRepository(private val roomDataDao: RoomDataDao) {

//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun add(vararg divingPoint:DivingPoint){
//        roomDataDao.add(*divingPoint)
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun getAll():List<DivingPoint>{
//        return roomDataDao.getAll()
//    }
//
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    fun getDivingPlaceByArea(areaTag :String):List<String>{
//        return roomDataDao.getDivingPlaceByArea(areaTag)
//    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(){
        roomDataDao.deleteAll()
    }
}