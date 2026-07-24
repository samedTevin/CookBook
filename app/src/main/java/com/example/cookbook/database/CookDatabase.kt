package com.example.cookbook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cookbook.dao.UserDao
import com.example.cookbook.model.User

@Database(entities = [User::class], version = 1)
abstract class CookDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        lateinit var database : CookDatabase

        fun createDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, CookDatabase::class.java, "user_db").build()
    }
}