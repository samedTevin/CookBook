package com.example.cookbook.repository

import com.example.cookbook.api.ApiService
import com.example.cookbook.model.Meal
import com.example.cookbook.model.MealResponse

class MealRepository(private val api: ApiService) {

    suspend fun searchMeal(name: String): MealResponse{
        return api.searchMeal(name)
    }
    suspend fun getDetails(id: String): Meal? {
        return api.getDetails(id).meals?.firstOrNull()
    }
    suspend fun getRandomMeal(): Meal?{
        return api.getRandomMeal().meals?.firstOrNull()
    }
    suspend fun filterByArea(name: String): MealResponse{
        return api.filterByArea(name)
    }

    suspend fun filterByCategory(categoryName: String): MealResponse{
        return api.filterByCategory(categoryName)
    }
}