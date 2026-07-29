package com.example.cookbook.event

sealed class HomeEvent {
    data class NavigateToDetail(val mealId: String): HomeEvent()
}