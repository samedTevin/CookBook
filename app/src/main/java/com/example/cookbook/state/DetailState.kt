package com.example.cookbook.state

import com.example.cookbook.model.Meal

sealed class DetailState {
    object Idle: DetailState()
    object Loading: DetailState()
    data class Success(val meal: Meal?): DetailState()
    data class Error(val message: String): DetailState()
}