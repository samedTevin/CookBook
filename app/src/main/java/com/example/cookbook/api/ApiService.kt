package com.example.cookbook.api

import com.example.cookbook.model.CategoryResponse
import com.example.cookbook.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search.php")
    suspend fun searchMeal(
        @Query("s") name: String
    ): MealResponse

    @GET("categories.php")
    suspend fun getCategories() : CategoryResponse
}