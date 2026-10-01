package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.User
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.state.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    fun registerUser(user: User){
        viewModelScope.launch {
            val existingUser = userRepository.findUser(user.email,user.password ?: "")

            if(existingUser != null){
                _registerState.value = RegisterState.EmailAlreadyExists
                _registerState.value = RegisterState.Idle
            }else{
                userRepository.insertUser(user)
                _registerState.value = RegisterState.Success
            }
        }
    }
}