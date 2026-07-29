package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.event.HomeEvent
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val mealRepository: MealRepository): ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState = _homeState.asStateFlow()

    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent = _homeEvent.asSharedFlow()
    
    private val _filter = MutableStateFlow<MealResponse?>(null)
    val filter = _filter.asStateFlow()

    fun getRandomMeal(){
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            try{
                val meal = mealRepository.getRandomMeal()

                _homeState.value = HomeState.Success(meal)
                _homeEvent.emit(HomeEvent.NavigateToDetail(meal.idMeal))
            }
            catch (e: Exception){
                _homeState.value = HomeState.Error("An error occurred...")
            }
        }
    }

    fun filterBySelectedCuisine(selectedCuisine: String){
        viewModelScope.launch {
            _filter.value = mealRepository.filterByArea(selectedCuisine)
        }
    }
}