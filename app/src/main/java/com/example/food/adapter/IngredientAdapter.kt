package com.example.food.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food.data.model.ExtendedIngredient
import com.example.food.databinding.IngredientRowLayoutBinding
import com.example.food.util.RecipesDiffUtil

class IngredientAdapter: RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {
    private var ingredients = emptyList<ExtendedIngredient>()

    class MyViewHolder(private val binding: IngredientRowLayoutBinding):RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup):MyViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = IngredientRowLayoutBinding.inflate(inflater,parent,false)
                return MyViewHolder(binding)
            }
        }

        fun bind(ingredient: ExtendedIngredient){
            binding.ingredient = ingredient
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {

        return ingredients.size
    }

    fun setData(newData: List<ExtendedIngredient>){
        val recipesDiffUtil = RecipesDiffUtil(ingredients, newData)
        val calculateDiff = DiffUtil.calculateDiff(recipesDiffUtil)
        ingredients = newData
        calculateDiff.dispatchUpdatesTo(this)
    }
}