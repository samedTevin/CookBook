package com.example.cookbook.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageManager {

    fun setLanguage(language: String){
        val locale = LocaleListCompat.forLanguageTags(language)

        AppCompatDelegate.setApplicationLocales(locale)
    }

}