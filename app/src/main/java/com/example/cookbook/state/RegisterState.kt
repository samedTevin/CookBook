package com.example.cookbook.state

sealed class RegisterState {
    object Idle: RegisterState()
    object Success: RegisterState()
    object EmailAlreadyExists: RegisterState()

}