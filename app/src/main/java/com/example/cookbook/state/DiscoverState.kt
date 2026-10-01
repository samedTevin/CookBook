package com.example.cookbook.state

import com.example.cookbook.model.MealResponse

sealed class DiscoverState {

    object Idle: DiscoverState()
    object Loading: DiscoverState()
    data class Success(val meal: MealResponse): DiscoverState()
    data class Error(val message: String): DiscoverState()

}