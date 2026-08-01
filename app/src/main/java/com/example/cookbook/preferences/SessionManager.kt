package com.example.cookbook.preferences

import android.content.SharedPreferences

class SessionManager(private val sharedPreferences: SharedPreferences) {

    fun logIn(){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
    }

    fun logOut(){
        sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
    }

    fun isLoggedIn() : Boolean{
        return sharedPreferences.getBoolean("isLoggedIn",false)
    }

    fun saveLanguage(language: String){
        sharedPreferences.edit().putString("language", language).apply()
    }

    fun getLanguage(): String?{
        return sharedPreferences.getString("language", null)
    }

    fun saveCuisine(cuisine: String){
        sharedPreferences.edit().putString("selectedCuisine",cuisine).apply()
    }

    fun getCuisine(): String?{
        return sharedPreferences.getString("selectedCuisine",null)
    }

    fun saveDarkMode(isEnabled: Boolean){
        sharedPreferences.edit().putBoolean("isDarkModeEnabled",isEnabled).apply()
    }

    fun getDarkMode(): Boolean{
        return sharedPreferences.getBoolean("isDarkModeEnabled", false)
    }

    fun saveIngredient(ingredient: String){
        sharedPreferences.edit().putString("selectedIngredient", ingredient).apply()
    }

    fun getIngredient(): String? {
        return sharedPreferences.getString("selectedIngredient", null)
    }



}