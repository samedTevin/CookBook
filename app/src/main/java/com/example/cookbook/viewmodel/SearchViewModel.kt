package com.example.cookbook.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import com.example.cookbook.state.SearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val mealRepository: MealRepository): ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _searchStateFlow = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchStateFlow = _searchStateFlow.asStateFlow()

    fun searchMeal(name: String){
        viewModelScope.launch {

            _searchStateFlow.value = SearchState.Loading

            val response = mealRepository.searchMeal(name)

            if(response.meals.isNullOrEmpty()){
                _searchStateFlow.value = SearchState.NotFound
            }
            else{
                _searchStateFlow.value = SearchState.Success(response.meals)
            }
        }
    }
    fun setIdle(){
        _searchStateFlow.value = SearchState.Idle
    }

    fun updateQuery(query: String){
        _query.value = query
    }
}