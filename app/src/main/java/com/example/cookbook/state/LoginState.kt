package com.example.cookbook.state


sealed class LoginState {

    object Idle : LoginState()
    object Loading: LoginState()
    object Success: LoginState()
    object UserNotFound: LoginState()
}