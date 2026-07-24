package com.example.cookbook.repository

import com.example.cookbook.api.ApiService
import com.example.cookbook.model.Category
import com.example.cookbook.model.CategoryResponse
import java.util.Locale

class CategoryRepository(private val api: ApiService) {

    suspend fun getCategories(): CategoryResponse{
       return api.getCategories()
    }
}