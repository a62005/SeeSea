package com.example.seesea.roomDB

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RoomDataApplication:Application(),ViewModelStoreOwner {
    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { RoomDataBase.getInstance(this, applicationScope) }
    val repository by lazy { RoomDataRepository(database.roomDataDao()) }
    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}