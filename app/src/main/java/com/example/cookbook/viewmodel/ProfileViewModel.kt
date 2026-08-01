package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.util.LanguageManager

class ProfileViewModel(private val userRepository: UserRepository,private val sessionManager: SessionManager): ViewModel() {

    fun logOut() = sessionManager.logOut()

    fun  setLanguage(language: String) = LanguageManager.setLanguage(language)
}