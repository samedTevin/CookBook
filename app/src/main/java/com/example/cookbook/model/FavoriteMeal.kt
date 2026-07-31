package com.example.cookbook.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMeal(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String?,
    val strCategory: String?,
    val strCountry: String?,
    val strMealThumb: String?
)
