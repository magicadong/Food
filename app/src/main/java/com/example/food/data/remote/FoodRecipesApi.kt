package com.example.food.data.remote

import com.example.food.data.model.FoodRecipes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface FoodRecipesApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
            @QueryMap queries: Map<String,String>
    ):Response<FoodRecipes>
}