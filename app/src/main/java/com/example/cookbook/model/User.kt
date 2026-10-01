package com.example.cookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("full_name")
    val fullName: String? = null,
    @ColumnInfo("username")
    val username: String?,
    @ColumnInfo("password")
    val password: String?,
    @ColumnInfo("image_path")
    val imagePath: String? = null,
    )
