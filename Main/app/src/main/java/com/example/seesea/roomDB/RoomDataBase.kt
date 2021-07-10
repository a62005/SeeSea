package com.example.seesea.roomDB

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Test::class], version = 1)
@TypeConverters(Converters::class)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun roomDataDao(): RoomDataDao

    private class RoomDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val roomDataDao = database.roomDataDao()
                    // Delete all content here.
                    roomDataDao.deleteAll()
                }
            }
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: RoomDataBase? = null
        fun getInstance(context: Context, scope: CoroutineScope): RoomDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext,
                        RoomDataBase::class.java,
                        "RoomDataBase")
                    .addCallback(RoomDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class Converters{
    @TypeConverter
    fun listToJson(value: List<String>?)= Gson().toJson(value)!!
    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toMutableList()

}