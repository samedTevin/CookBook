package com.example.cookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val email: String,
    @ColumnInfo("full_name")
    val fullName: String,
    @ColumnInfo("username")
    val username: String,
    @ColumnInfo("password")
    val password: String,
    )
