package com.example.cookbook.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.repository.FavoriteMealRepository
import com.example.cookbook.viewmodel.FavoriteViewModel
import com.example.cookbook.viewmodel.HomeViewModel

class FavoriteViewModelFactory(private val favoriteMealRepository: FavoriteMealRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(favoriteMealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}