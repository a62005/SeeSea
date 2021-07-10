package com.example.seesea.room

import java.io.File
import java.io.Serializable


data class ActivityRoom(

    val activityId:Int,//房間ID
    val hostId:Int,
    val hostName:String,//主辦人名字
    val hostImage:String,
    val activityName:String,//房間名稱-長度15
    val activityStatusCode:Int,//房間狀態
    val activityPicture:String,//圖片路徑
    val currentParticipantNumber:Int,//報名人數
    val participantNumber:Int,//參加人數
    val participantIDList:List<String>,//參加者的userID
    val divingType:String,//潛水種類
    val divingLevel:String,//潛水LEVEL
    val activityProperty:String,
    val activityDate:String,//日期
    val activityTime:String,//時間
    val activityArea:String,//區域
    val activityPlace:String,//地點-長度10
    val transportation:String,//交通方式
    val activityDescription:String,//說明/介紹-長度200
    val meetingPlace:String,//見面地點-長度20
    val messageBoard:MutableList<Message>,
    val applicantImage:List<String>,
    var favoriteNumber:Int,
    var isFavorite:Boolean = false,//本地端使用

    val divingTypingCode:Int,
    val divingLevelCode:Int,
    val activityAreaCode:Int,
    val transportationCode:Int,
    val estimateCost:String,
):Serializable

data class Message(
    val messageId:Int,
    val userId:Int,
    val userName:String,
    val userImage:String,
    val message:String,
    val messageDateTime:String
):Serializable

data class NewMessage(
    val activityId: Int,
    val userId:Int,
    val message:String,
    val messageDateTime:String
)

data class PreviewRoom(
    val hostId:Int,
    val activityName:String,//房間名稱-長度15
    val activityPicture: File,//圖片路徑
    val participantNumber:Int,//參加人數
    val divingTypeCode:Int,//潛水種類
    val divingLevelCode:Int,//潛水LEVEL
    val activityPropertyCode:Int,//揪團性質
    val activityDateTime:String,//日期時間
    val activityAreaCode:Int,//區域
    val activityPlace:String,//地點-長度10
    val transportationCode:Int,//交通方式
    val activityDescription:String,//說明/介紹-長度200
    val meetingPlace:String,
    val estimateCostCode:Int,
    var activityPlaceCode:Int = 0
):Serializable

data class PBoxRoom(
    val activityId:Int,
    val hostId:Int,
    val activityName:String,
    val activityStatusCode:Int,
    val divingType:String,
    val activityPlace:String,
    val activityProperty:String
)
data class PBoxRoomList(
    val participatingRoomList:List<PBoxRoom>,
    val signingUpRoomList:List<PBoxRoom>,
    val endingRoomList:List<PBoxRoom>
)

data class ModifyRoom(
    val activityId: Int,
    val activityName: String,
    val activityPicture: File,
    val divingLevelCode: Int,
    val activityPlaceCode: Int,
    val transportationCode: Int,
    val activityDescription: String,
    val meetingPlace: String,
    val estimateCostCode: Int
)


