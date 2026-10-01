package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.DetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val mealRepository: MealRepository): ViewModel() {
    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)
    val state = _state.asStateFlow()

    fun getDetails(id: String){
        _state.value = DetailState.Loading
        viewModelScope.launch {
            try {
               val detail = mealRepository.getDetails(id)
                _state.value = DetailState.Success(detail)
            }catch (e: Exception){
                _state.value = DetailState.Error(e.localizedMessage)
            }
        }
    }
}