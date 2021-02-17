package com.example.food.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.food.data.local.entity.FavoritesEntity
import com.example.food.data.local.entity.RecipesEntity
import com.example.food.util.RecipesTypeConverter

@Database(
        entities = [RecipesEntity::class,FavoritesEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase:RoomDatabase() {
    abstract fun recipesDao():RecipesDao
}