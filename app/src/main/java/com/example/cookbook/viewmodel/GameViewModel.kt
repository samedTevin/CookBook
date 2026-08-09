package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.MealResponse
import com.example.cookbook.repository.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(private val mealRepository: MealRepository): ViewModel() {
    private var _list = MutableStateFlow<MealResponse?>(null)
    val list = _list.asStateFlow()
    private var _selectedLetter = MutableStateFlow<String>("?")
    val selectedLetter = _selectedLetter.asStateFlow()

    fun listByFirstLetter(letter: String){
        viewModelScope.launch {
            _list.value = mealRepository.listByFirstLetter(letter)
        }
    }

    fun saveLetter(letter: String){
        viewModelScope.launch {
            _selectedLetter.value = letter
        }
    }
}