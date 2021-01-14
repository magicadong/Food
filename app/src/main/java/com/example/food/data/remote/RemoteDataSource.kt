package com.example.food.data.remote

import com.example.food.data.model.FoodRecipes
import com.example.food.util.log
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val recipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries: Map<String,String>):Response<FoodRecipes>{
        log("RemoteDataSource start")
        val result = recipesApi.getRecipes(queries)
        log("RemoteDataSource end:${result.body()}")
        return result
        //return recipesApi.getRecipes(queries)
    }
}