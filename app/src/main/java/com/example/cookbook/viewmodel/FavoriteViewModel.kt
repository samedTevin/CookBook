package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.FavoriteMeal
import com.example.cookbook.model.Meal
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.state.FavState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteMealRepository: FavoriteMealRepository): ViewModel() {

    private val _favState = MutableStateFlow<FavState>(FavState.Idle)
    val favState = _favState.asStateFlow()



    fun collectFavorites(){
        viewModelScope.launch {
            _favState.value = FavState.Loading
            try{
                favoriteMealRepository.getAllFavoriteMeal().collect { favoriteMeals ->
                    _favState.value = FavState.Success(favoriteMeals)
                }

            }catch (e: Exception){
                _favState.value = FavState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite(meal: Meal){
        viewModelScope.launch {
            favoriteMealRepository.toggleFavorite(meal)
        }
    }

    fun deleteFavorite(favMeal: FavoriteMeal){
        viewModelScope.launch {
            favoriteMealRepository.deleteFavorite(favMeal)
        }
    }

    fun isFavorite(id: String): Flow<Boolean> {
        return favoriteMealRepository.isFavorite(id)
    }

}