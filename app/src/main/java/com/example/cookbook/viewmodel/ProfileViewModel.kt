package com.example.cookbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.User
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.util.LanguageManager
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository,private val sessionManager: SessionManager): ViewModel() {



    fun logOut() = sessionManager.logOut()

    fun  setLanguage(language: String) = LanguageManager.setLanguage(language)

    fun getUserEmail(): String? = sessionManager.getCurrentUserEmail()

    fun updateUserPhoto(userEmail: String, imagePath: String?) = viewModelScope.launch {
        userRepository.updateProfilePhoto(userEmail, imagePath)
    }

    fun getUserByEmail(email: String): LiveData<User?>{
        return userRepository.findUserForProfilePhoto(email)
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }
}