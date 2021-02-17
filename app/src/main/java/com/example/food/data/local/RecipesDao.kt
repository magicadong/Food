package com.example.food.data.local

import androidx.room.*
import com.example.food.data.local.entity.FavoritesEntity
import com.example.food.data.local.entity.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favoritesEntity: FavoritesEntity)

    @Query("select * from recipes_table")
    fun readRecipes():Flow<List<RecipesEntity>>

    @Query("select * from favorites_table order by id asc")
    fun readFavorites():Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavorite(favoritesEntity: FavoritesEntity)

    @Query("delete from favorites_table")
    suspend fun deleteAllFavorites()
}