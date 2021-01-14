package com.example.food.data.remote

import com.example.food.data.model.FoodRecipes
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val recipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String,String>):Response<FoodRecipes>{
        return recipesApi.getRecipes(queries)
    }
}