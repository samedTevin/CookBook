package com.example.cookbook.repository

import com.example.cookbook.dao.FavoriteMealDao
import com.example.cookbook.mapper.toFavoriteMeal
import com.example.cookbook.model.FavoriteMeal
import com.example.cookbook.model.Meal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FavoriteMealRepository(private val favoriteMealDao: FavoriteMealDao) {

    suspend fun toggleFavorite(meal: Meal){
        val favMeal = meal.toFavoriteMeal()

        if(favoriteMealDao.isFavorite(favMeal.idMeal).first()){
            favoriteMealDao.deleteFavoriteMeal(favMeal)
        }
        else{
            favoriteMealDao.insertFavoriteMeal(favMeal)
        }
    }

    suspend fun deleteFavorite(favMeal: FavoriteMeal){
        delay(850)
        favoriteMealDao.deleteFavoriteMeal(favMeal)
    }

    fun getAllFavoriteMeal(): Flow<List<FavoriteMeal>> {
        return favoriteMealDao.getAllFavoriteMeal()
    }

    fun isFavorite(id: String): Flow<Boolean> {
        return favoriteMealDao.isFavorite(id)
    }

    suspend fun clearAllFavorites() {
        favoriteMealDao.clearAllFavorites()
    }
}