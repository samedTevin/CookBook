package com.example.cookbook.repository

import android.content.SharedPreferences
import com.example.cookbook.dao.UserDao
import com.example.cookbook.model.User
import com.example.cookbook.preferences.SessionManager

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    suspend fun findUser(email: String): User?{
        val user = userDao.findUser(email)
        return user
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }
    
    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

}