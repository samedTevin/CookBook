package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.DiscoverState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(private val mealRepository: MealRepository): ViewModel() {


    private val _state = MutableStateFlow<DiscoverState?>(null)
    val state = _state.asStateFlow()

    fun filterByArea(name: String){
        _state.value = DiscoverState.Loading
        viewModelScope.launch {
            try{
                val mealByArea = mealRepository.filterByArea(name)
                _state.value = DiscoverState.Success(mealByArea)
            }catch (e: Exception){
                _state.value = DiscoverState.Error(e.localizedMessage)
            }
        }
    }

    fun filterByCategory(categoryName: String){
        _state.value = DiscoverState.Loading
        viewModelScope.launch {
            try{
                val mealByCategory = mealRepository.filterByCategory(categoryName)
                _state.value = DiscoverState.Success(mealByCategory)
            }catch (e: Exception){
                _state.value = DiscoverState.Error(e.localizedMessage)
            }
        }
    }
}