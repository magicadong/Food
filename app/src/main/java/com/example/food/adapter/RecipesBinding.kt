package com.example.food.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.food.data.local.RecipesEntity
import com.example.food.data.model.FoodRecipes
import com.example.food.util.NetworkResult

class RecipesBinding {
    companion object{
        @BindingAdapter("readApiResponse","readDatabase",requireAll = true)
        @JvmStatic
        fun handleReadError(
            view: View,
            apiResponse: NetworkResult<FoodRecipes>?,
            database:List<RecipesEntity>?
        ){
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
            if (view is TextView){
                view.text = apiResponse?.message
            }
        }
    }
}
