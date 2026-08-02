package com.example.cookbook.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
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

    fun findUserForProfilePhoto(email: String): LiveData<User?>{
       return userDao.findUserForProfile(email)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }
    
    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun updateProfilePhoto(userEmail: String, imagePath: String?){
        userDao.updateProfilePhoto(userEmail, imagePath)
    }

}