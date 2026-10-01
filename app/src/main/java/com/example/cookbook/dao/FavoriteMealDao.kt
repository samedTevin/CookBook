package com.example.cookbook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cookbook.model.FavoriteMeal
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal)

    @Delete
    suspend fun deleteFavoriteMeal(favoriteMeal: FavoriteMeal)

    @Query("SELECT * FROM FavoriteMeal")
    fun getAllFavoriteMeal(): Flow<List<FavoriteMeal>>

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteMeal WHERE idMeal = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Query("DELETE FROM FavoriteMeal")
    suspend fun clearAllFavorites()
}