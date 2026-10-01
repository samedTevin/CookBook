package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.event.HomeEvent
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.state.CardState
import com.example.cookbook.state.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val mealRepository: MealRepository, private val userRepository: UserRepository): ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState = _homeState.asStateFlow()

    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()

    private val _homeEvent = MutableSharedFlow<HomeEvent>()
    val homeEvent = _homeEvent.asSharedFlow()
    
    private val _cuisine = MutableStateFlow<CardState>(CardState.Idle)
    val cuisine = _cuisine.asStateFlow()

    private val _ingredient = MutableStateFlow<CardState>(CardState.Idle)
    val ingredient = _ingredient.asStateFlow()

    fun getRandomMeal(){
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
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
        _cuisine.value = CardState.Loading
        viewModelScope.launch {
            try {
                val filterByCuisine = mealRepository.filterByArea(selectedCuisine)
                _cuisine.value = CardState.Success(filterByCuisine)
            }catch (e: Exception){
                _cuisine.value = CardState.Error(e.localizedMessage)
            }
        }
    }

    fun filterBySelectedIngredient(selectedIngredient: String){
        _ingredient.value = CardState.Loading
        viewModelScope.launch {
            try {
                val filterByIngredient = mealRepository.filterByIngredient(selectedIngredient)
                _ingredient.value = CardState.Success(filterByIngredient)
            }catch (e: Exception){
                _ingredient.value = CardState.Error(e.localizedMessage)
            }
        }
    }

    fun findUsername(email: String){
        viewModelScope.launch {
            _username.value = userRepository.findUsername(email)
        }
    }
}