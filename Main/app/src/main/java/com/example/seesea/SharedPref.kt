package com.example.seesea

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.seesea.user.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class SharedPref private constructor() {

    companion object {
        private val sharePref = SharedPref()
        private lateinit var sharedPreferences: SharedPreferences
        private const val KEY = "user"

        fun getInstance(context: Context): SharedPref {
            if (!::sharedPreferences.isInitialized) {
                synchronized(SharedPref::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }

    fun getUser():User?{
        val str: String = sharedPreferences.getString(KEY, "") ?: return null
        val gson = GsonBuilder().create()
        return gson.fromJson<User>(str, object : TypeToken<User>() {}.type)
    }

    fun saveUser(user:User) {
        val str:String = Gson().toJson(user)
        sharedPreferences.edit()
            .putString(KEY, str)
            .apply()
    }

    fun updateUser(user: User){
        clearAll()
        saveUser(user)
    }

    fun removeUser() {
        sharedPreferences.edit().remove(KEY).apply()
    }

    private fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

}