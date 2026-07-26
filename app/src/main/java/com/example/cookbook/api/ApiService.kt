package com.example.cookbook.api

import com.example.cookbook.model.CategoryResponse
import com.example.cookbook.model.Meal
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

    @GET("lookup.php")
    suspend fun getDetails(@Query("i") id: String): MealResponse

    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    @GET("filter.php")
    suspend fun filterByArea(@Query("a") name: String): MealResponse
}