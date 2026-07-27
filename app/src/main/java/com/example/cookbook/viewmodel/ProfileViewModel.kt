package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.UserRepository

class ProfileViewModel(private val userRepository: UserRepository,private val sessionManager: SessionManager): ViewModel() {

    fun logOut() = sessionManager.logOut()
}