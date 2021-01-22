package com.example.food.util

class Constants {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "1a0edebda73f4a17ad82375357e41313"
        const val IMAGE_BASE_URL = "https://spoonacular.com/cdn/ingredients_250x250/"

        // Query Key
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_NUMBER = "number"
        const val QUERY_QUERY = "query"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"
        // Database
        const val RECIPES_TABLE_NAME = "recipes_table"
        const val DATABASE_NAME = "recipes_database"

        // DataStore
        const val PREFERENCES_NAME = "foodPreference"
        const val MEAL_TYPE_KEY = "mealType"
        const val MEAL_ID_KEY = "mealId"
        const val DIET_TYPE_KEY = "dietType"
        const val DIET_ID_KEY = "dietID"
        const val BACK_ONLINE_KEY = "backOnline"

    }
}