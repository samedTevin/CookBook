package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.Meal
import com.example.cookbook.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val mealRepository: MealRepository): ViewModel() {
    private val _randomMeal = MutableStateFlow<Meal?>(null)
    val randomMeal = _randomMeal.asStateFlow()

    fun getRandomMeal(){
        viewModelScope.launch {
           _randomMeal.value = mealRepository.getRandomMeal()
        }
    }
}