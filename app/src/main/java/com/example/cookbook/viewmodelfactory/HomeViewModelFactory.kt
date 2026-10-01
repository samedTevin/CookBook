package com.example.cookbook.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.repository.UserRepository
import com.example.cookbook.viewmodel.DetailViewModel
import com.example.cookbook.viewmodel.HomeViewModel

class HomeViewModelFactory(private val mealRepository: MealRepository, private val userRepository: UserRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(mealRepository,userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}