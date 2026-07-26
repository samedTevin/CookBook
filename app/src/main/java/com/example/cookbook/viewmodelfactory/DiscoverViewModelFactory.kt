package com.example.cookbook.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.viewmodel.DiscoverViewModel

class DiscoverViewModelFactory(private val mealRepository: MealRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoverViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiscoverViewModel(mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}