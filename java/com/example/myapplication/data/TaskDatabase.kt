package com.example.myapplication.data

import android.app.Application
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class],version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao():TaskDao

    class Callback @Inject constructor(
        val database: Provider<TaskDatabase>,
        private val applicationScope:CoroutineScope
    ):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()
            applicationScope.launch {
                dao.insert(Task("Develop Mvvm app"))
                dao.insert(Task("Eat lunch",true))
                dao.insert(Task("Go bronze",false,true))
                dao.insert(Task("Sleep"))
            }
        }
    }
}