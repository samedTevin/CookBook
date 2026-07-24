package com.example.cookbook.state

import com.example.cookbook.model.Meal

sealed class SearchState {
    object Idle: SearchState()
    object Loading: SearchState()
    data class Success(val meal: List<Meal>): SearchState()
    object NotFound: SearchState()
}