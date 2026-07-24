package com.example.cookbook.repository

import com.example.cookbook.api.ApiService
import com.example.cookbook.model.MealResponse

class MealRepository(private val api: ApiService) {

    suspend fun searchMeal(name: String): MealResponse{
        return api.searchMeal(name)
    }
}