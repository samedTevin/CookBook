package com.example.cookbook.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cookbook.model.User
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email AND password =:password")
    suspend fun findUser(email: String, password: String) : User?

    @Query("SELECT * FROM User WHERE email = :email")
    fun findUserForProfile(email: String): LiveData<User?>

    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE user SET image_path = :imagePath WHERE email = :userEmail ")
    suspend fun updateProfilePhoto(userEmail: String, imagePath: String?)
}