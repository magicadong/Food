package com.example.food.util

import androidx.room.TypeConverter
import com.example.food.data.model.FoodRecipes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    val gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipes: FoodRecipes):String {
        return gson.toJson(foodRecipes)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String):FoodRecipes{
        val type = object : TypeToken<FoodRecipes>(){}.type
        return gson.fromJson(data,type)
    }
}