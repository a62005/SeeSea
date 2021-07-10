package com.example.seesea

import android.graphics.Bitmap
import android.util.Log
import com.example.seesea.room.*
import com.example.seesea.roomDB.DivingPoint
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.user.ModifyUser
import com.example.seesea.user.NewUser
import com.example.seesea.user.ParticipantUser
import com.example.seesea.user.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.resumeWithException

private const val ROOM_DATA_URL = "http://35.221.136.55/api/GroupPairing/"
private const val API_URL = "http://35.221.136.55/api/"
private const val USER_DATA_URL = "http://35.221.136.55/api/UserInfo"
private const val DIVING_POINT = "http://35.221.136.55/api/DivingPoint"

class OkHttp {

    //JSON連結

    //創建client
    private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    val get = Get()
    val post = Post()
    val put = Put()
    val delete = Delete()

    inner class Get{
        suspend fun getRoomList():List<ActivityRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}ROOM").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}ROOM")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ActivityRoom>>(body, object : TypeToken<List<ActivityRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET RoomList請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得所有房間
        suspend fun getUserRoomList(userId:Int):PBoxRoomList{
            val request = Request.Builder().url("${ROOM_DATA_URL}UserRoomList/$userId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}UserRoomList/$userId")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<PBoxRoomList>(body, object : TypeToken<PBoxRoomList>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET UserRoomList請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得使用者相關房間
        suspend fun getDivingPointRoomById(divingPointId:Int):List<PBoxRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}Room/Place/$divingPointId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/Place/$divingPointId")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<PBoxRoom>>(body, object : TypeToken<List<PBoxRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET RoomByIds請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得IDs房間
        suspend fun getRoomByIds(roomIds:String):List<ActivityRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}Room/$roomIds").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/$roomIds")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ActivityRoom>>(body, object : TypeToken<List<ActivityRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET RoomByIds請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得IDs房間
        suspend fun getRoomById(roomId:String):ActivityRoom{
            val request = Request.Builder().url("${ROOM_DATA_URL}Room/RoomID/$roomId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/RoomID/$roomId")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<ActivityRoom>(body, object : TypeToken<ActivityRoom>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            println(response.code)
                            it.resumeWithException(Exception("API-GET Room請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得ID房間
        suspend fun getActiveRoom():List<ActivityRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}ActiveRoom").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}ActiveRoom")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ActivityRoom>>(body, object : TypeToken<List<ActivityRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET ActiveRoom請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得舉辦中的房間
        suspend fun getFilterRoom(divingType:String,property:String,area:String,cost:String):List<ActivityRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}Room/Select?divingType=$divingType&property=$property&area=$area&estimateCost=$cost").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/Select?divingType=$divingType&property=$property&area=$area&estimateCost=$cost")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ActivityRoom>>(body, object : TypeToken<List<ActivityRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET FilterRoom請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得舉辦中的房間

        suspend fun getSearchRoom():List<ActivityRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}ActiveRoom").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}ActiveRoom")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ActivityRoom>>(body, object : TypeToken<List<ActivityRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET SearchRoom請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得舉辦中的房間
        suspend fun getRecommendRoom(userId:Int):List<ActivityRoom>{
            val request = Request.Builder().url("${ROOM_DATA_URL}UserPreferRoom/$userId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}UserPreferRoom/$userId")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ActivityRoom>>(body, object : TypeToken<List<ActivityRoom>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-GET RecommendRoom請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取得舉辦中的房間

        suspend fun getOneUserData(userId:Int): User {
            val request = Request.Builder().url("${USER_DATA_URL}/$userId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${API_URL}UserInfo?account=")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<User>(body, object : TypeToken<User>() {}.type)
                            Log.d("test","Api result $result")
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }

        suspend fun getParticipantUser(roomId:Int): List<ParticipantUser> {
            val request = Request.Builder().url("${ROOM_DATA_URL}Room/$roomId/ParticipantList").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/$roomId/ParticipantList")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ParticipantUser>>(body, object : TypeToken<List<ParticipantUser>>() {}.type)
                            Log.d("test","Api result $result")
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun getSignUpUser(roomId:Int): List<ParticipantUser> {
            val request = Request.Builder().url("${ROOM_DATA_URL}Room/$roomId/ApplicantList").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/$roomId/ApplicantList")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<ParticipantUser>>(body, object : TypeToken<List<ParticipantUser>>() {}.type)
                            Log.d("test","Api result $result")
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }

        suspend fun getCheckAccountRepeat(account: String):Boolean {
            val request = Request.Builder().url("${USER_DATA_URL}/CheckUserAccount?account=$account").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/CheckUserAccount?account=$account")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            if(body == "true"){
                                it.resumeWith(Result.success(true))
                            }else{
                                it.resumeWith(Result.success(false))
                            }

                            println("Response: $body")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun getCheckEmailRepeat(email: String):Boolean {
            val request = Request.Builder().url("${USER_DATA_URL}/CheckUserEmail?eMail=$email").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/CheckUserEmail?eMail=$email")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            if(body == "true"){
                                it.resumeWith(Result.success(true))
                            }else{
                                it.resumeWith(Result.success(false))
                            }
                            println("Response: $body")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun getCheckPhoneRepeat(phone: Int):Boolean {
            val request = Request.Builder().url("${USER_DATA_URL}/CheckUserPhone?phoneNumber=$phone").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/CheckUserPhone?phoneNumber=$phone")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            if(body == "true"){
                                it.resumeWith(Result.success(true))
                            }else{
                                it.resumeWith(Result.success(false))
                            }
                            println("Response: $body")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }

        suspend fun getDivingPoint():List<DivingPoint> {
            val request = Request.Builder().url(DIVING_POINT).build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: $DIVING_POINT")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<List<DivingPoint>>(body, object : TypeToken<List<DivingPoint>>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $body")
                        }else{
                            it.resumeWithException(Exception("取得資料失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
    }

    inner class Post{
        suspend fun postLogin(account:String,password:String):User {
            val body = JSONObject()
                .put("account",account)
                .put("password",password)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${API_URL}UserInfo/Login").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${API_URL}UserInfo/Login")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<User>(body, object : TypeToken<User>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("登入失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //使用者登入
        suspend fun postCheckLogin(account:String,password:String):Boolean{
            val body = JSONObject()
                .put("account",account)
                .put("password",password)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${USER_DATA_URL}/Login/Validation").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/Login/Validation")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<Boolean>(body, object : TypeToken<Boolean>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("密碼錯誤"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //使用者前確認帳號密碼
        suspend fun postCreateUser(user:NewUser) {
            val body = Gson().toJson(user)
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url(USER_DATA_URL).build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: $USER_DATA_URL")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            println("Response: $body")
                        }else{
                            it.resumeWithException(Exception("註冊失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //使用者登入

        suspend fun postPreviewRoom(room:PreviewRoom){
            val bos = ByteArrayOutputStream()
            RoomDataViewModel.bitmap?.compress(Bitmap.CompressFormat.JPEG,100,bos)
            val bitmapData = bos.toByteArray()
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("HostId","${room.hostId}")
                .addFormDataPart("ActivityName", room.activityName)
                .addFormDataPart("ActivityPicture",room.activityPicture.name.toString(),
                    bitmapData.toRequestBody("image/*jpg".toMediaTypeOrNull()))
                .addFormDataPart("ParticipantNumber","${room.participantNumber}")
                .addFormDataPart("DivingTypeCode","${room.divingTypeCode}")
                .addFormDataPart("DivingLevelCode","${room.divingLevelCode}")
                .addFormDataPart("ActivityPropertyCode","${room.activityPropertyCode}")
                .addFormDataPart("ActivityDateTime",room.activityDateTime)
                .addFormDataPart("ActivityAreaCode","${room.activityAreaCode}")
                .addFormDataPart("ActivityPlaceCode","${room.activityPlaceCode}")
                .addFormDataPart("TransportationCode","${room.transportationCode}")
                .addFormDataPart("ActivityDescription",room.activityDescription)
                .addFormDataPart("MeetingPlace",room.meetingPlace)
                .addFormDataPart("EstimateCostCode","${room.estimateCostCode}")
                .build()
            val request = Request.Builder().post(body).url("${ROOM_DATA_URL}Room").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            Log.d("test","post api result $result")
                        }else{
                            it.resumeWithException(Exception("API-POST Room請求失敗"))
                            Log.d("test","${response.code}  ${response.body?.string()}")
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //創建房間
        suspend fun postSignUp(roomId: Int,userId:Int,signUpDescription:String,applicatingDateTime:String){
            val body = JSONObject()
                .put("activityId",roomId)
                .put("applicantId",userId)
                .put("applicatingDescription",signUpDescription)
                .put("applicatingDateTime",applicatingDateTime)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${ROOM_DATA_URL}Room/Applicant").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/Applicant")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            Log.d("test","post api result $result")
                        }else{
                            println(response.code)
                            it.resumeWithException(Exception("API-POST SignUp請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //報名
        suspend fun postAgreeSignUp(roomId: Int,userId:Int){
            val body = JSONObject()
                .put("activityId",roomId)
                .put("applicantId",userId)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${ROOM_DATA_URL}Room/Participant?activityId=${roomId}&userId=${userId}").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/Participant?activityId=${roomId}&userId=${userId}")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            Log.d("test","post api result $result")
                        }else{
                            it.resumeWithException(Exception("API-POST SignUp請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //主辦者同意報名

        suspend fun postSignIn(user:User){
            val body = Gson().toJson(user)
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            Log.d("test","$requestBody")
            val request = Request.Builder().post(requestBody).url(USER_DATA_URL).build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: $USER_DATA_URL")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            Log.d("test","post api result $result")
                        }else{
                            it.resumeWithException(Exception("API-POST 註冊請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //創建帳號
        suspend fun postMessage(message: NewMessage):Int{
            val body = Gson().toJson(message)
            Log.d("test",body)
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${ROOM_DATA_URL}Room/Message").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/Message")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<Int>(body, object : TypeToken<Int>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            println(response.code)
                            it.resumeWithException(Exception("API-新增留言請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //創建帳號

        suspend fun postAddFavoriteRoom(roomId: Int,userId: Int){
            val body = JSONObject()
                .put("activityId",roomId)
                .put("applicantId",userId)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${USER_DATA_URL}/$userId/FavoriteRoomList?activityId=$roomId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/$userId/FavoriteRoomList?activityId=$roomId")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            Log.d("test","post api result $result")
                        }else{
                            it.resumeWithException(Exception("API-POST 關注房間請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //新增關注房間
        suspend fun postCheckOldPassword(userId: Int,oldPassword:String):Boolean{
            val body = JSONObject()
                .put("userId",userId)
                .put("password",oldPassword)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().post(requestBody).url("${USER_DATA_URL}/Login/PasswordCorrection").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/Login/PasswordCorrection")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<Boolean>(body, object : TypeToken<Boolean>() {}.type)
                            it.resumeWith(Result.success(result))
                            println("Response: $result")
                        }else{
                            println(response.code)
                            it.resumeWithException(Exception("API-修改密碼請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }

        } //創建帳號
    }

    inner class Put{
        suspend fun putUpdateUserExperience(userId: Int,experienceCode:Int){
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserExperienceCode",experienceCode.toString())
                .build()
            val request = Request.Builder().put(body).url("${USER_DATA_URL}/UserId/${userId}").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/UserId/${userId}")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-USER更新資料請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun putDeclareFull(roomId: Int){
            val body = JSONObject()
                .put("activityId",roomId)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().put(requestBody).url("${ROOM_DATA_URL}Room/$roomId/ActivityStatus").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/$roomId/ActivityStatus")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-PUT 宣告滿團請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun putForgotPassword(account: String,email: String){
            val body = JSONObject()
                .put("account",account)
                .put("email",email)
                .toString()
            val requestBody = body.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder().put(requestBody).url("${USER_DATA_URL}/UserAccount/$account/PasswordForgotten?userEmail=$email").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/UserAccount/$account/PasswordForgotten?userEmail=$email")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-PUT 忘記密碼請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun putModifyRoom(modifyRoom: ModifyRoom){
            val bos = ByteArrayOutputStream()
            RoomDataViewModel.bitmap?.compress(Bitmap.CompressFormat.JPEG,100,bos)
            val bitmapData = bos.toByteArray()

            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ActivityName", modifyRoom.activityName)
                .addFormDataPart("ActivityPicture",modifyRoom.activityPicture.name.toString(),
                    bitmapData.toRequestBody("image/*jpg".toMediaTypeOrNull()))
                .addFormDataPart("DivingLevelCode","${modifyRoom.divingLevelCode}")
                .addFormDataPart("ActivityPlaceCode","${modifyRoom.activityPlaceCode}")
                .addFormDataPart("TransportationCode","${modifyRoom.transportationCode}")
                .addFormDataPart("ActivityDescription",modifyRoom.activityDescription)
                .addFormDataPart("MeetingPlace",modifyRoom.meetingPlace)
                .addFormDataPart("EstimateCostCode","${modifyRoom.estimateCostCode}")
                .build()
            val request = Request.Builder().put(body).url("${ROOM_DATA_URL}Room/${modifyRoom.activityId}").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/${modifyRoom.activityId}")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result ${response.code}")
                        }else{
                            println("${response.code}")
                            it.resumeWithException(Exception("API-PUT 修改房間請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
        suspend fun putModifyUser(modifyUser: ModifyUser):User{
            val bos = ByteArrayOutputStream()
            RoomDataViewModel.bitmap?.compress(Bitmap.CompressFormat.JPEG,100,bos)
            val bitmapData = bos.toByteArray()
            val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("UserPassword",modifyUser.userPassword)
                .addFormDataPart("UserNickName",modifyUser.userNickName)
                .addFormDataPart("UserPhone","${modifyUser.userPhone}")
                .addFormDataPart("UserEmail", modifyUser.userEmail)
                .addFormDataPart("UserImage",modifyUser.userImage.name.toString(),
                    bitmapData.toRequestBody("image/*jpg".toMediaTypeOrNull()))
                .addFormDataPart("UserDescription",modifyUser.userDescription)
                .addFormDataPart("DivingTypeTag",modifyUser.divingTypeTag)
                .addFormDataPart("AreaTag",modifyUser.areaTag)
                .build()
            val request = Request.Builder().put(body).url("${USER_DATA_URL}/UserId/${modifyUser.userId}").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/UserId/${modifyUser.userId}")
                        if (response.isSuccessful) {
                            val body = response.body?.string()
                            val result = gson.fromJson<User>(body, object : TypeToken<User>() {}.type)
                            println("Response: $result")
                            it.resumeWith(Result.success(result))
                        }else{
                            println("${response.code} ${response.request.body.toString()}")
                            it.resumeWithException(Exception("API-PUT 修改USER請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        }
    }

    inner class Delete{
        suspend fun deleteQuitSignUp(roomId: Int, userId:Int){
            val request = Request.Builder().delete().url("${ROOM_DATA_URL}Room/${roomId}/Applicant/${userId}").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/${roomId}/Applicant/${userId}")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-取消報名請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //取消報名
        suspend fun deleteQuitActivity(roomId: Int, userId:Int){
            val request = Request.Builder().delete().url("${ROOM_DATA_URL}Room/${roomId}/Participant/${userId}").build()
            val call = client.newCall(request)
            Log.d("test","delete")
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/${roomId}/Participant/${userId}")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-退出活動請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //參加者離開活動
        suspend fun deleteParticipant(roomId: Int,userId:Int){
            val request = Request.Builder().delete().url("${ROOM_DATA_URL}Room/${roomId}/Participant/${userId}").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/${roomId}/Participant/${userId}")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-剔除參加者請求失敗"))
                        }

                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //主辦者剔除已參加活動的使用這

        suspend fun deleteFavoriteRoom(roomId: Int,userId:Int){
            val request = Request.Builder().delete().url("${USER_DATA_URL}/$userId/FavoriteRoomList?activityId=$roomId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${USER_DATA_URL}/$userId/FavoriteRoomList?activityId=$roomId")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-剔除關注房間請求失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //剔除關注活動
        suspend fun deleteMessage(messageId:Int){
            val request = Request.Builder().delete().url("${ROOM_DATA_URL}Room/Message?messageId=$messageId").build()
            val call = client.newCall(request)
            return suspendCancellableCoroutine {
                call.enqueue(object : Callback {
                    //若失敗則印出警告訊息
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute Request")
                        it.resumeWithException(e)
                    }
                    //若成功則印出內容
                    override fun onResponse(call: Call, response: Response) {
                        println("RequestUrl: ${ROOM_DATA_URL}Room/Message?messageId=$messageId")
                        if (response.isSuccessful) {
                            val result = response.body?.string()
                            println("Response: $result")
                        }else{
                            it.resumeWithException(Exception("API-剔除留言請求失敗"))
                        }
                    }
                })
                it.invokeOnCancellation {
                    call.cancel()
                }
            }
        } //剃除留言
    }

}