package com.example.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.food.data.local.entity.FavoritesEntity
import com.example.food.databinding.FavoriteRowLayoutBinding
import com.example.food.util.RecipesDiffUtil

class FavoriteRecipesAdapter: RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {
    private var favoriteList:List<FavoritesEntity> = emptyList()

    class MyViewHolder(private val binding:FavoriteRowLayoutBinding):
        RecyclerView.ViewHolder(binding.root){
            companion object{
                fun from(parent: ViewGroup): MyViewHolder{
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = FavoriteRowLayoutBinding.inflate(layoutInflater,parent,false)
                    return MyViewHolder(binding)
                }
            }

        fun bind(favoritesEntity: FavoritesEntity){
            binding.favoriteEntity = favoritesEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    fun setData(newFavoriteList: List<FavoritesEntity>){
        val diffUtil = RecipesDiffUtil<FavoritesEntity>(favoriteList,newFavoriteList)
        val calculateDiff = DiffUtil.calculateDiff(diffUtil)
        favoriteList = newFavoriteList
        calculateDiff.dispatchUpdatesTo(this)
    }
}