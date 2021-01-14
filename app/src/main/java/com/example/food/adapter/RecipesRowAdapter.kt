package com.example.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food.data.model.FoodRecipes
import com.example.food.data.model.Result
import com.example.food.databinding.RecipesRowLayoutBinding
import com.example.food.util.RecipesDiffUtil

class RecipesRowAdapter: RecyclerView.Adapter<RecipesRowAdapter.RecipeViewHolder>() {
    private var recipes: List<Result> = emptyList()

    class RecipeViewHolder(
            private val binding: RecipesRowLayoutBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):RecipeViewHolder{
                val inflator = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding
                        .inflate(inflator,parent,false)
                return RecipeViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(newData: FoodRecipes){
        val recipesDiffUtil = RecipesDiffUtil(recipes,newData.results)
        val diffResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffResult.dispatchUpdatesTo(this)
    }
}