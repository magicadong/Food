package com.example.food.data

import com.example.food.data.local.RecipesDao
import com.example.food.data.local.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
      private val recipesDao: RecipesDao
) {
     fun readRecipes():Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipe(recipesEntity: RecipesEntity){
        recipesDao.insertRecipe(recipesEntity)
    }
}