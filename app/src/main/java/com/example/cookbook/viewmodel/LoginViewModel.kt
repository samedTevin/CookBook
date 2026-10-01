package com.example.cookbook.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.preferences.SessionManager
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository, private val sessionManager: SessionManager): ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String){
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val user = userRepository.findUser(email,password)

            if(user != null){
                _loginState.value = LoginState.Success
            }
            else{
                _loginState.value = LoginState.UserNotFound
                _loginState.value = LoginState.Idle
            }
        }
    }

    fun setLoggedIn(){
        sessionManager.logIn()
    }
}