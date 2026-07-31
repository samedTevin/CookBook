package com.example.cookbook.mapper

import com.example.cookbook.model.FavoriteMeal
import com.example.cookbook.model.Ingredient
import com.example.cookbook.model.Meal

fun Meal.toIngredients(): List<Ingredient>{

    val list = mutableListOf<Ingredient>()

    val ingredients = listOf(
        strIngredient1,
        strIngredient2,
        strIngredient3,
        strIngredient4,
        strIngredient5,
        strIngredient6,
        strIngredient7,
        strIngredient8,
        strIngredient9,
        strIngredient10,
        strIngredient11,
        strIngredient12,
        strIngredient13,
        strIngredient14,
        strIngredient15,
        strIngredient16,
        strIngredient17,
        strIngredient18,
        strIngredient19,
        strIngredient20
    )

    val measures = listOf(
        strMeasure1,
        strMeasure2,
        strMeasure3,
        strMeasure4,
        strMeasure5,
        strMeasure6,
        strMeasure7,
        strMeasure8,
        strMeasure9,
        strMeasure10,
        strMeasure11,
        strMeasure12,
        strMeasure13,
        strMeasure14,
        strMeasure15,
        strMeasure16,
        strMeasure17,
        strMeasure18,
        strMeasure19,
        strMeasure20
    )

    for (i in ingredients.indices){
        val ingredient = ingredients[i]
        val measure = measures[i]

        if(!ingredient.isNullOrBlank()){
            list.add(
                Ingredient(
                    ingredient,
                    measure.orEmpty(),
                    "https://www.themealdb.com/images/ingredients/${ingredient.lowercase().replace(" ", "_")}.png/medium"
                )
            )
        }
    }

    return list
}

fun Meal.toFavoriteMeal(): FavoriteMeal{

    return FavoriteMeal(idMeal, strMeal, strCategory, strCountry, strMealThumb)
}