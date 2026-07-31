package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.FavoriteMeal
import com.example.cookbook.model.Meal
import com.example.cookbook.repository.FavoriteMealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteMealRepository: FavoriteMealRepository): ViewModel() {


    val favorites = favoriteMealRepository.getAllFavoriteMeal()

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