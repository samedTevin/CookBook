package com.example.cookbook.state

import com.example.cookbook.model.MealResponse

sealed class CardState {
    object Idle: CardState()
    object Loading: CardState()
    data class Success(val meal: MealResponse): CardState()
    data class Error(val message: String): CardState()
}