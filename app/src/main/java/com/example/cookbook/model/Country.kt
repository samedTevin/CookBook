package com.example.cookbook.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Country(val apiName: String, @StringRes val nameRes: Int, @DrawableRes val imgRes: Int)
