package com.example.seesea.roomDB

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.seesea.ChipSetter
import com.example.seesea.OkHttp
import com.example.seesea.room.*
import com.example.seesea.user.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.random.Random

class RoomDataViewModel(private val repository:RoomDataRepository): ViewModel() {
    companion object{
        var bitmap :Bitmap? = null //
    }

    private val okHttp = OkHttp()

    val roomListPage: RoomList = RoomList()
    val roomPage = Room()
    val createRoomPage = CreateRoom()
    val userPage = Host()
    val pBoxPage = PBox()
    val loginPage = LoginPage()
    val mapPage = Map()

    var user = MutableLiveData<User>()
    var userId = 0

    inner class RoomList {//關於活動列表
        var searchChip = MutableLiveData<List<String>>()
        private var searchChipByInt = mutableListOf<MutableList<Int>>()
        private var searchChipByStr = mutableListOf<MutableList<String>>()

        val allRoomList = MutableLiveData<List<ActivityRoom>>()
        var favoriteRoom = MutableLiveData<List<ActivityRoom>>()
        var participatingRoom = MutableLiveData<List<PBoxRoom>>()
        var signingUpRoom = MutableLiveData<List<PBoxRoom>>()
        var finishedRoom = MutableLiveData<List<PBoxRoom>>()
        var recommendRoom = MutableLiveData<List<ActivityRoom>>()

        fun loadList() {//獲取舉辦中(含滿團)的活動
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        try {
                            allRoomList.postValue(okHttp.get.getActiveRoom())
                        }catch (e:Exception){
                            println("$e 取得活動列表失敗")
                        }
                        getFavoriteRoomList()
                    }
                }

            }
        }

        private fun getFavoriteRoomList(){//獲取關注活動
            if(user.value == null){
                return
            }
            val tmpUser = user.value!!
            val tmp = tmpUser.userFavoriteRoom
            val str = ChipSetter.intTranslateToStr(tmp.toList())
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        try {
                            favoriteRoom.postValue(okHttp.get.getRoomByIds(str))
                        }catch (e:Exception){
                            println("$e 取得關注列表失敗")
                        }
                    }
                }

            }
        }

        fun getRecommendRoom(){//獲取推薦活動
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        try {
                            recommendRoom.postValue(okHttp.get.getRecommendRoom(userId))
                        }catch (e:Exception){
                            println("$e 取得推薦列表失敗")
                        }
                    }
                }

            }
        }

        fun setOneRecommendRoom(){//當無推薦活動則隨機從舉辦中的活動取2顯示
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val tmp = mutableListOf<ActivityRoom>()
                        tmp.add(allRoomList.value!!.random())
                        tmp.add(allRoomList.value!!.random())
                        launch {
                            recommendRoom.postValue(tmp)
                        }
                    }
                }
            }

        }

        fun addFavorite(roomId: Int,room:ActivityRoom){//把活動加入我的最愛
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val tmpUser = user.value!!
                        val tmp = favoriteRoom.value!!.toMutableList()
                        tmpUser.userFavoriteRoom.add(roomId)
                        tmp.add(room)
                        launch {
                            try {
                                okHttp.post.postAddFavoriteRoom(roomId, userId)
                            }catch (e:Exception){
                                println("$e 加入關注失敗")
                            }
                        }
                        user.postValue(tmpUser)
                        favoriteRoom.postValue(tmp)
                    }
                }
            }

        }

        fun deleteFavorite(roomId: Int,room:ActivityRoom){//把活動剃除我的最愛
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val tmpUser = user.value!!
                        val tmp = favoriteRoom.value!!.toMutableList()
                        tmpUser.userFavoriteRoom.remove(roomId)
                        tmp.remove(room)
                        launch {
                            try{
                                okHttp.delete.deleteFavoriteRoom(roomId, userId)
                            }catch (e:Exception){
                                println("$e 剔除關注失敗")
                            }

                        }
                        user.postValue(tmpUser)
                        favoriteRoom.postValue(tmp)
                    }
                }
            }

        }

        fun searchFilter(divingType:String,property:String,area:String,cost:String){//使用filter搜尋的活動
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        try {
                            allRoomList.postValue(okHttp.get.getFilterRoom(divingType,property, area, cost))
                        }catch (e:Exception){
                            println("$e 搜尋活動失敗")
                        }
                    }
                }
            }
        }

        fun cancelSearchChip(key:String){//當點擊filter chip 的cancel時會依cancel後的chip繼續做filter
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val tmpStrArr = searchChipByStr
                        val tmpIntArr = searchChipByInt
                        for(i in tmpStrArr.indices){
                            for(k in tmpStrArr[i].indices){
                                if(key == tmpStrArr[i][k]){
                                    tmpStrArr[i].removeAt(k)
                                    tmpIntArr[i].removeAt(k)
                                    break
                                }
                            }
                        }
                        val tmpArr = mutableListOf<String>()
                        for (i in tmpStrArr){
                            tmpArr.addAll(i)
                        }
                        setSearchChip(tmpArr,tmpStrArr,tmpIntArr)
                        searchFilter(ChipSetter.intTranslateToStr(tmpIntArr[0]),ChipSetter.intTranslateToStr(tmpIntArr[1]),
                            ChipSetter.intTranslateToStr(tmpIntArr[2]),ChipSetter.intTranslateToStr(tmpIntArr[3]))
                    }
                }
            }

        }

        fun setSearchChip(chipArr:MutableList<String>,strArr:MutableList<MutableList<String>>,intArr:MutableList<MutableList<Int>>){//顯示當前filter chip
            searchChipByStr = strArr
            searchChipByInt = intArr
            searchChip.postValue(chipArr)

        }
    }
    inner class CreateRoom {//關於創建活動頁面
        var previewRoom = MutableLiveData<PreviewRoom>()
        var divingPlace = MutableLiveData<List<String>>()

        fun setPreviewRoom(room: PreviewRoom) {
            previewRoom.postValue(room)
        }

        fun postPreviewRoom() {
            val pre = previewRoom.value!!
            viewModelScope.launch {
                var placeCode = 0
                mapPage.allDivingPointArr.forEach {if(pre.activityPlace == it.divingPointName) placeCode = it.divingPointId}
                pre.activityPlaceCode = placeCode
                try {
                    okHttp.post.postPreviewRoom(pre)
                }catch (e:Exception){
                    println("$e 創建活動失敗")
                }
            }
        }

        fun getDivingPlaceByArea(areaTag:String){
            val tmp = mutableListOf<String>()
            viewModelScope.launch {
                mapPage.allDivingPointArr.forEach { if(it.areaTag == areaTag) tmp.add(it.divingPointName) }
                divingPlace.postValue(tmp.toList())
            }
        }
    }
    inner class Room{
        var currentRoom = MutableLiveData<ActivityRoom>()
        var currentRoomMessage = MutableLiveData<List<Message>>()

        fun signUp(roomId:Int,describe:String,time:String){//報名
            val tmpUser = user.value!!
            tmpUser.userSigningUpActivity.add(roomId)
            viewModelScope.launch {
                try {
                    okHttp.post.postSignUp(roomId,userId,describe,time)
                }catch (e:Exception){
                    println("$e 報名活動失敗")
                }
            }
            user.postValue(tmpUser)

        }

        fun setCurrentRoom(room:ActivityRoom){
            currentRoom.postValue(room)
            currentRoomMessage.postValue(room.messageBoard)
        }

        fun getHostData(guestId:Int){
            if(guestId == userId){
                userPage.guest.postValue(user.value)
                return
            }
            viewModelScope.launch {
                try {
                    userPage.guest.postValue(okHttp.get.getOneUserData(guestId))
                }catch (e:Exception){
                    println("$e 取得主辦人資訊失敗")
                }
            }
        }

        fun getRoomById(roomId: Int){
            viewModelScope.launch {
                try {
                    val tmp = okHttp.get.getRoomById(roomId.toString())
                    currentRoomMessage.postValue(tmp.messageBoard)
                    currentRoom.postValue(tmp)
                }catch (e:Exception){
                    println("$e 取得活動資訊失敗")
                }
            }
        }

        fun quitSignUp(){//取消報名
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val roomId = currentRoom.value!!.activityId
                        val tmpUser = user.value!!
                        launch {
                            try {
                                okHttp.delete.deleteQuitSignUp(roomId,userId)
                            }catch (e:Exception){
                                println("$e 報名活動失敗")
                            }
                        }
                        tmpUser.userSigningUpActivity.remove(roomId)
                        user.postValue(tmpUser)
                    }
                }
            }

        }

        fun pullOutActivity(){//參加者退出活動
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val roomId = currentRoom.value!!.activityId
                        val tmpUser = user.value!!
                        launch {
                            try {
                                okHttp.delete.deleteQuitActivity(roomId, userId)
                            }catch (e:Exception){
                                println("$e 參加者退出活動失敗")
                            }

                        }
                        tmpUser.userParticipatingActivity.remove(roomId)
                        user.postValue(tmpUser)
                    }
                }
            }

        }

        fun addMessage(message: NewMessage){
            val tmp = currentRoom.value!!
            val user = user.value!!
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    try {
                        val id = async {
                            okHttp.post.postMessage(message)
                        }.await()
                        val tmpMessage = Message(
                            id,
                            userId,
                            user.userNickName,
                            user.userImage,
                            message.message,
                            message.messageDateTime
                        )
                        tmp.messageBoard.add(tmpMessage)
                        currentRoom.postValue(tmp)
                        currentRoomMessage.postValue(tmp.messageBoard)
                    }catch (e:Exception){
                        println("$e 新增留言失敗")
                    }
                }
            }
        }

        fun deleteMessage(position:Int,messageId:Int){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val tmp = currentRoom.value!!
                        tmp.messageBoard.removeAt(position)
                        launch {
                            try {
                                okHttp.delete.deleteMessage(messageId)
                            }catch (e:Exception){
                                println("$e 剔除留言失敗")
                            }

                        }
                        currentRoom.postValue(tmp)
                        currentRoomMessage.postValue(tmp.messageBoard)
                    }
                }
            }

        }

        fun declareFull(){
            val roomId = currentRoom.value!!.activityId
            viewModelScope.launch {
                try {
                    okHttp.put.putDeclareFull(roomId)
                }catch (e:Exception){
                    println("$e 宣告滿團失敗")
                }

            }
            viewModelScope.launch {
                roomPage.getRoomById(roomId)
                roomListPage.allRoomList
            }
        }

        fun modifyRoom(modifyRoom: ModifyRoom){
            viewModelScope.launch {
                try {
                    okHttp.put.putModifyRoom(modifyRoom)
                }catch (e:Exception){
                    println("$e 修改活動資訊失敗")
                }
            }
            viewModelScope.launch {
                getRoomById(currentRoom.value!!.activityId)
            }
        }

    }
    inner class Host{

        private var divingTag = listOf<String>()
        private var areaTag = listOf<String>()

        var lastUserTag = MutableLiveData<MutableList<String>>()
        var participantUser = MutableLiveData<List<ParticipantUser>>()
        var signUpUser = MutableLiveData<List<ParticipantUser>>()

        var guest = MutableLiveData<User>()
        var userType = MutableLiveData<String>()

        fun getParticipantUser(){
            val roomId = roomPage.currentRoom.value!!.activityId
            viewModelScope.launch {
                try {
                    signUpUser.postValue(okHttp.get.getSignUpUser(roomId))
                    participantUser.postValue(okHttp.get.getParticipantUser(roomId))
                }catch (e:Exception){
                    println("$e 獲取活動中的使用者失敗")
                }

            }
        }

        fun agreeSignUp(userId:Int){//同意報名
            val roomId = roomPage.currentRoom.value!!.activityId
            val tmp = signUpUser.value!!.toMutableList()
            for(i in tmp){
                if(i.applicantId == userId){
                    tmp.remove(i)
                    break
                }
            }
            viewModelScope.launch {
                try {
                    okHttp.post.postAgreeSignUp(roomId,userId)
                    signUpUser.postValue(tmp)
                }catch (e:Exception){
                    println("$e 同意報名失敗")
                }

            }

        }

        fun quitParticipant(userId:Int){//把參加者退出
            val roomId = roomPage.currentRoom.value!!.activityId
            val tmpArr= participantUser.value!!.toMutableList()
            for(i in tmpArr){
                if(i.participantId == userId){
                    tmpArr.remove(i)
                    break
                }
            }
            viewModelScope.launch {
                try {
                    okHttp.delete.deleteParticipant(roomId, userId)
                    participantUser.postValue(tmpArr)
                }catch (e:Exception){
                    println("$e 主辦人剔除參加者失敗")
                }
            }

        }

        fun removeUserTag(tag:String){
            val tmp = user.value!!
            val arr = lastUserTag.value
            tmp.areaTag.remove(tag)
            tmp.divingTypeTag.remove(tag)
            arr?.add(tag)
            viewModelScope.launch {
                user.postValue(tmp)
                lastUserTag.postValue(arr)
            }

        }

        fun addUserTag(tag:String){
            val tmp = user.value!!
            val arr = lastUserTag.value!!
            if(areaTag.contains(tag)){
                tmp.areaTag.add(tag)
            }
            if(divingTag.contains(tag)){
                tmp.divingTypeTag.add(tag)
            }
            arr.remove(tag)
            viewModelScope.launch {
                user.postValue(tmp)
                lastUserTag.postValue(arr)
            }
        }

        fun setLastUserTag(areaTag:MutableList<String>,divingTag:MutableList<String>){
            this.areaTag = areaTag.toList()
            this.divingTag = divingTag.toList()
            val tmp = user.value!!
            tmp.areaTag.forEach { userTag -> if(areaTag.contains(userTag)){areaTag.remove(userTag)}}
            tmp.divingTypeTag.forEach { userTag -> if(divingTag.contains(userTag)){divingTag.remove(userTag)}}
            areaTag.addAll(divingTag)
            lastUserTag.postValue(areaTag)
        }

        fun updateExperience(){
            val tmpUser = user.value!!
            tmpUser.userExperience = USER_EXPERIENCE[1]
            viewModelScope.launch {
                try {
                    okHttp.put.putUpdateUserExperience(userId,2)
                }catch (e:Exception){
                    println("$e 修改使用者經驗失敗")
                }

            }
            user.postValue(tmpUser)
        }

        fun setUserType(type:String){
            userPage.userType.postValue(type)
        }

        fun getUserData(){
//            val user = user.value!!
            viewModelScope.launch {
                try {
                    val tmp = okHttp.get.getOneUserData(userId)
                    user.postValue(tmp)
                }catch (e:Exception){
                    println("$e 取得主辦人資訊失敗")
                }
            }
        }
    }
    inner class PBox{
        fun loadUserRoomList(){
            viewModelScope.launch{
                withContext(Dispatchers.IO) {
                    try {
                        val tmp = async {
                            okHttp.get.getUserRoomList(userId)
                        }.await()
                        roomListPage.participatingRoom.postValue(tmp.participatingRoomList)
                        roomListPage.signingUpRoom.postValue(tmp.signingUpRoomList)
                        roomListPage.finishedRoom.postValue(tmp.endingRoomList)
                    }catch (e:Exception){
                        println("$e 讀取使用者相關活動失敗")
                    }

                }
            }
        }
    }
    inner class LoginPage{
        var canLogin = MutableLiveData<Boolean>()
        var checkAccount = MutableLiveData<Boolean>()
        var checkEmail = MutableLiveData<Boolean>()
        var checkPhone = MutableLiveData<Boolean>()

        fun canLogin(account:String, password:String){
            viewModelScope.launch {
                try {
                    canLogin.postValue(okHttp.post.postCheckLogin(account, password))
                }catch (e:Exception){
                    println("$e 確認帳號是否正確失敗")
                }
            }
        }
        fun login(account:String, password:String){
            viewModelScope.launch {
                try {
                    user.postValue(okHttp.post.postLogin(account,password))
                }catch (e:Exception){
                    println("$e 登入失敗")
                }

            }
        }

        fun setUser(loginUser: User){
            user.postValue(loginUser)
            userId = loginUser.userId
        }

        fun checkOldPassword(oldPassword:String){
            viewModelScope.launch {
                try {
                    canLogin.postValue(okHttp.post.postCheckOldPassword(userId,oldPassword))
                }catch (e:Exception){
                    println("$e 確認密碼是否正確失敗")
                }

            }
        }

        fun checkEmailRepeat(email:String){
            viewModelScope.launch {
                try {
                    checkEmail.postValue(okHttp.get.getCheckEmailRepeat(email))
                }catch (e:Exception){
                    println("$e 確認信箱是否重複失敗")
                }

            }
        }

        fun checkAccountRepeat(account: String){
            viewModelScope.launch {
                try {
                    checkAccount.postValue(okHttp.get.getCheckAccountRepeat(account))
                }catch (e:Exception){
                    println("$e 確認帳號是否重複失敗")
                }

            }
        }

        fun checkPhoneRepeat(phone:Int){
            viewModelScope.launch {
                try {
                    checkPhone.postValue(okHttp.get.getCheckPhoneRepeat(phone))
                }catch (e:Exception){
                    println("$e 確認手機是否重複失敗")
                }

            }
        }

        fun createUser(user:NewUser){
            viewModelScope.launch {
                try {
                    okHttp.post.postCreateUser(user)
                }catch (e:Exception){
                    println("$e 創建帳號失敗")
                }
            }
        }

        fun forgotPassword(account: String,email: String){
            viewModelScope.launch {
                try {
                    okHttp.put.putForgotPassword(account, email)
                }catch (e:Exception){
                    println("$e 通知忘記密碼失敗")
                }

            }
        }

        fun modifyUser(modifyUser: ModifyUser){
            viewModelScope.launch {
                try {
                    user.postValue(okHttp.put.putModifyUser(modifyUser))
                }catch (e:Exception){
                    println("$e 修改使用者資料失敗")
                }

            }
        }
    }
    inner class Map{

        var allDivingPointArr = mutableListOf<DivingPoint>()
        val displayDivingPoint = MutableLiveData<List<DivingPoint>>()
        val searchHint = MutableLiveData<String>()

        var onMarkerClick = MutableLiveData<List<Double>>() //判斷點擊Marker的當前DP ID
        val searchDivingPointName = MutableLiveData<List<DivingPoint>>()

        val holdingActivity = MutableLiveData<List<PBoxRoom>>()

        fun loadingDivingPoint() {
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        try {
                            allDivingPointArr = okHttp.get.getDivingPoint().toMutableList()
                            displayDivingPoint.postValue(allDivingPointArr)
                        }catch (e:Exception){
                            println("$e 讀取潛點資料失敗")
                        }
                    }
                }
            }
        }

        fun resetDivingPoint(){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        displayDivingPoint.postValue(allDivingPointArr)
                    }
                }
            }
        }

        fun loadingDivingPoint(divingPoints: List<DivingPoint>){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                    displayDivingPoint.postValue(divingPoints)
                    }
                }
            }
        }

        fun setSinglePoint(divingPoint: DivingPoint){
            val tmp = mutableListOf<DivingPoint>()
            tmp.add(divingPoint)
            viewModelScope.launch {
                displayDivingPoint.postValue(tmp)
                searchHint.postValue(divingPoint.divingPointName)
            }
        }

        fun deleteAllPoint(){
            viewModelScope.launch {
                repository.deleteAll()
            }
        }

        fun mapSearchByKey(key: String){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        val tmp = mutableListOf<DivingPoint>()
                        for(i in allDivingPointArr){
                            if(i.divingPointName.contains(key)){
                                tmp.add(i)
                            }
                        }
                        launch {
                            searchDivingPointName.postValue(tmp)
                        }
                    }
                }
            }
        }

        fun mapSearchByChip(area:String,divingType:String,level:String){
            val tmpArr = mutableListOf<DivingPoint>()
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    launch {
                        allDivingPointArr.forEach {
                            if(area == ""){
                                tmpArr.addAll(allDivingPointArr)
                                return@forEach
                            }
                            if(it.areaTag == area)tmpArr.add(it)
                        }
                        val tmp = mutableListOf<DivingPoint>()
                        tmpArr.forEach { dp->
                            if(divingType == ""){
                                tmp.addAll(tmpArr)
                                return@forEach
                            }
                            dp.divingTypeTag.forEach { if(it == divingType) tmp.add(dp) }
                        }

                        tmpArr.clear()
                        tmp.forEach {
                            if(level == ""){
                                tmpArr.addAll(tmp)
                                return@forEach
                            }
                            if(it.divingDifficulty == level)tmpArr.add(it)
                        }
                        val str = setHint(area,divingType,level)
                        launch {
                            displayDivingPoint.postValue(tmpArr)
                            searchHint.postValue(str)
                        }
                    }
                }
            }
        }

        private fun setHint(area:String,divingType:String,level:String):String{
            var str = area
            if(divingType != "") str += "/$divingType"
            if(level != "") str += "/$level"
            return str

        }

        fun setOnClickMarkerNumber(title:String){
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    val tmpArr = displayDivingPoint.value!!
                    for (i in tmpArr.indices){
                        if(tmpArr[i].divingPointName == title){
                            val tmp = mutableListOf<Double>()
                            launch {
                                tmp.add(i.toDouble())
                                tmp.add(tmpArr[i].latitude)
                                tmp.add(tmpArr[i].longitude)
                                onMarkerClick.postValue(tmp)
                            }
                        }
                    }
                }
            }

        }

        fun getDivingPointActivity(divingPointId:Int){
            viewModelScope.launch {
                try {
                    holdingActivity.postValue(okHttp.get.getDivingPointRoomById(divingPointId))
                }catch (e:Exception){
                    println("$e 從潛點獲取活動訊息失敗")
                }

            }
        }
    }
}

class RoomDataViewModelFactory(private val repository: RoomDataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomDataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}