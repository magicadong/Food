package com.example.food.data

import com.example.food.data.local.RecipesDao
import com.example.food.data.local.entity.FavoritesEntity
import com.example.food.data.local.entity.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
      private val recipesDao: RecipesDao
) {
    fun readRecipes():Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavorites():Flow<List<FavoritesEntity>>{
        return recipesDao.readFavorites()
    }

    suspend fun insertRecipe(recipesEntity: RecipesEntity){
        recipesDao.insertRecipe(recipesEntity)
    }

    suspend fun insertFavorite(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavorites(favoritesEntity)
    }

    suspend fun deleteFavorite(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavorite(favoritesEntity)
    }

    suspend fun deleteAllFavorites(){
        recipesDao.deleteAllFavorites()
    }
}