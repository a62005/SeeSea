package com.example.seesea.user

import androidx.room.Entity
import java.io.File
import java.io.Serializable

val USER_EXPERIENCE = arrayListOf("無經驗","一般","資深")

@Entity(tableName = "user_table")
data class User(
    val userId:Int,
    val userAge:Int,
    val userAccount:String,
    val userName:String,
    val userNickName:String,
    val userImage:String,
    val userPhone:Int,
    val userEmail:String,
    val isAcccountActive:Boolean,
    var userExperience:String,
    val divingTypeTag:MutableList<String>,
    val userDescription:String,
    val areaTag:MutableList<String>,

    val userFavoriteRoom:MutableList<Int>,
    val userParticipatingActivity:MutableList<Int>,
    var userSigningUpActivity:MutableList<Int>,
    val userFinishActivity:MutableList<Int>,
):Serializable

data class NewUser(
    val userAccount:String,
    val userPassword:String,
    val userName:String,
    val userNickName:String,
    val userAge:Int,
    val userPhone:Int,
    val userEmail:String,
    val divingTypeTag:String,
    val userExperienceCode:Int,
    val userIdentityCode:Int,
    val areaTag:String

)

data class ParticipantUser(
    val participantId:Int,
    val participantName:String,
    val participantImage:String,

    val applicantId:Int,
    val applicantName:String,
    val applicantImage:String,
    val applicatingDateTime:String,
    val applicatingDescription:String

)

data class ModifyUser(
    val userId: Int,
    val userPassword:String,
    val userNickName: String,
    val userPhone: Int,
    val userEmail: String,
    val userExperienceCode: Int,
    val userImage: File,
    val userDescription: String,
    val divingTypeTag: String,
    val areaTag: String,
)