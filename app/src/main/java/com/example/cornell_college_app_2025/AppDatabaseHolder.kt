package com.example.cornell_college_app_2025

import AppDatabase
import android.content.Context
import androidx.room.Room

object AppDatabaseHolder {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(this) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Schedule"
                ).build()
            }
        }
        return instance!!
    }
}