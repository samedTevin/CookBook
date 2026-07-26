package com.example.cookbook.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.viewmodel.CategoryViewModel
import com.example.cookbook.viewmodel.DetailViewModel

class DetailViewModelFactory(private val mealRepository: MealRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}