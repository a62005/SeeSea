package com.example.seesea

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.seesea.databinding.*
import com.example.seesea.login.LoginActivity
import com.example.seesea.room.RoomManagerFragment
import com.example.seesea.roomDB.RoomDataApplication
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.roomDB.RoomDataViewModelFactory
import com.example.seesea.user.User
import com.google.android.material.tabs.TabLayoutMediator

/***
 * author
 *      Android - YiLin Heish
 *      Backend - SzuChi Chen
 */

const val LOGOUT = "LOGOUT"
const val LOGIN = "LOGIN"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),RoomManagerFragment.Logout {

    private val viewModel: RoomDataViewModel by viewModels {
        RoomDataViewModelFactory((application as RoomDataApplication).repository)
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sp by lazy { SharedPref.getInstance(applicationContext) }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.mapPage.loadingDivingPoint()
        val user = sp.getUser()
        if(user is User){
            viewModel.loginPage.setUser(user)
        }else{
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //設定Bottom viewpager
        val icon: ArrayList<Drawable> = arrayListOf(
            resources.getDrawable(R.drawable.home,null),
            resources.getDrawable(R.drawable.global,null))

        binding.apply {
            val pageAdapter = MainPageAdapter(supportFragmentManager, lifecycle)
            viewPage2Main.adapter = pageAdapter
            TabLayoutMediator(tabLayoutBottomBar,viewPage2Main){  tab, position->
                tab.icon = icon[position]
            }.attach()
            viewPage2Main.isUserInputEnabled = false
        }
    }

    override fun onRestart() {
        super.onRestart()
        val user = intent.extras?.getSerializable(USER_SERVICE)
        if(user is User){
            viewModel.loginPage.setUser(user)
        }

    }

    override fun onStop() {
        if(viewModel.user.value != null){
            sp.updateUser(viewModel.user.value!!)
        }
        super.onStop()
    }

    override fun onResume() {
        val user = intent.extras?.getSerializable(LOGIN)
        if(user is User){
            viewModel.loginPage.setUser(user)
        }
        super.onResume()
    }

    override fun logout(keyWord:String){
        if(keyWord == LOGOUT){
            sp.removeUser()
            intent.setClass(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}