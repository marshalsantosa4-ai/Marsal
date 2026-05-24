package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.ProgressRepository

class UTApplication : Application() {
    lateinit var database: AppDatabase
    lateinit var repository: ProgressRepository

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "ut_portal_db"
        ).build()
        repository = ProgressRepository(database.progressDao())
    }
}
