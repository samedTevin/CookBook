package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val mealRepository: MealRepository): ViewModel() {
    private val _detail = MutableStateFlow<Meal?>(null)
    val detail = _detail.asStateFlow()

    fun getDetails(id: String){
        viewModelScope.launch {
            _detail.value = mealRepository.getDetails(id)
        }
    }
}