package com.example.seesea.chatBox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.seesea.SharedPref
import com.example.seesea.databinding.ActivityChatBoxMainBinding
import com.example.seesea.roomDB.RoomDataApplication
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.roomDB.RoomDataViewModelFactory

class ChatBoxMainActivity : AppCompatActivity() {

    private val viewModel: RoomDataViewModel by viewModels {
        RoomDataViewModelFactory((application as RoomDataApplication).repository)
    }
    private val binding by lazy { ActivityChatBoxMainBinding.inflate(layoutInflater) }
    private val sp by lazy { SharedPref.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}