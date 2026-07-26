package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(private val mealRepository: MealRepository): ViewModel() {
    private val _filter = MutableStateFlow<MealResponse?>(null)
    val filter = _filter.asStateFlow()

    fun filterByArea(name: String){
        viewModelScope.launch {
            _filter.value = mealRepository.filterByArea(name)
        }
    }
}