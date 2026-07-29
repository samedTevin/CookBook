package com.example.cookbook.state

import com.example.cookbook.model.Meal

sealed class HomeState {
    object Idle: HomeState()
    object Loading: HomeState()
    data class Success(val meal: Meal): HomeState()
    data class Error(val message: String): HomeState()
}