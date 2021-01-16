package com.example.food.util

class Constants {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "1a0edebda73f4a17ad82375357e41313"

        // Query Key
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_NUMBER = "number"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Database
        const val RECIPES_TABLE_NAME = "recipes_table"
        const val DATABASE_NAME = "recipes_database"

    }
}