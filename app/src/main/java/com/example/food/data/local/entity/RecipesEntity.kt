package com.example.food.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.food.data.model.FoodRecipes
import com.example.food.util.Constants.Companion.RECIPES_TABLE_NAME

@Entity(tableName = RECIPES_TABLE_NAME)
class RecipesEntity(
        var foodRecipes: FoodRecipes
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}