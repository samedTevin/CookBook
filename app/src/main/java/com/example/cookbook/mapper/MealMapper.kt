package com.example.cookbook.mapper

import android.text.SpannableString
import android.text.Spanned
import androidx.core.text.HtmlCompat
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

fun String?.formatInstructionsToHtml(): Spanned {
    if (this.isNullOrBlank()) return SpannableString("")

    val text = this
        .replace(Regex("(?i)<br\\s*/?>"), "\n")
        .replace("\r\n", "\n")
        .replace("\r", "\n")
        .trim()

    val stepHeaderRegex = Regex("(?i)^STEP\\s*\\d+[:\\.]?\\s*")
    val rawLines = text.split("\n").map { it.trim() }.filter { it.isNotBlank() }

    val steps = mutableListOf<String>()

    for (line in rawLines) {
        val cleanedLine = line
            .replace(stepHeaderRegex, "")
            .replace(Regex("^\\d+[\\.\\)]\\s*"), "")
            .replace(Regex("^[•\\-\\*]\\s*"), "")
            .trim()

        if (cleanedLine.isNotBlank()) {
            steps.add(cleanedLine)
        }
    }

    if (steps.size <= 1) {
        val singleText = if (steps.isNotEmpty()) steps[0] else text
        val sentences = singleText
            .split(Regex("(?<=\\.)\\s+(?=[A-Z])"))
            .map { it.trim() }
            .filter { it.isNotBlank() && it.length > 3 }

        if (sentences.size > 1) {
            steps.clear()
            steps.addAll(sentences)
        }
    }

    if (steps.isEmpty()) {
        return HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    val htmlString = steps.mapIndexed { index, step ->
        "<b>Step ${index + 1}</b><br/>$step"
    }.joinToString("<br/><br/>")

    return HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_LEGACY)
}