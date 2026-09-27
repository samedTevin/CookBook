package com.example.cookbook.state

import com.example.cookbook.model.FavoriteMeal

sealed class FavState {

    object Idle: FavState()
    object Loading: FavState()
    data class Success(val meals: List<FavoriteMeal>): FavState()
    data class Error(val message: String): FavState()
}