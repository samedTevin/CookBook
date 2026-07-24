package com.example.cookbook.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.viewmodel.LoginViewModel
import com.example.cookbook.viewmodel.RegisterViewModel

class LoginViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.Factory {
    // Boilerplate
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}