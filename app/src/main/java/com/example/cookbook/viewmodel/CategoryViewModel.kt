package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.api.ApiService
import com.example.cookbook.model.CategoryResponse
import com.example.cookbook.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryRepository: CategoryRepository): ViewModel() {
    private val _category = MutableStateFlow<CategoryResponse?>(null)
    val category = _category.asStateFlow()

    fun getCategories(){
        viewModelScope.launch {
            _category.value = categoryRepository.getCategories()
        }
    }



}