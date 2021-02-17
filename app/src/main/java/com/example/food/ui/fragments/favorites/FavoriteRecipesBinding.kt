package com.example.food.ui.fragments.favorites

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.food.adapter.FavoriteRecipesAdapter
import com.example.food.data.local.entity.FavoritesEntity

class FavoriteRecipesBinding {
    companion object{
        @BindingAdapter("viewVisibility","setData",requireAll = false)
        @JvmStatic
        fun setDataAndVisibility(
                view:View,
                favoritesEntityList: List<FavoritesEntity>?,
                mAdapter: FavoriteRecipesAdapter?
        ){
            if (favoritesEntityList.isNullOrEmpty()){
                when(view){
                    is RecyclerView -> view.visibility = View.INVISIBLE
                    is ImageView -> view.visibility = View.VISIBLE
                    is TextView -> view.visibility = View.VISIBLE
                }
            }else{
                when(view){
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoritesEntityList)
                    }
                    is ImageView -> view.visibility = View.INVISIBLE
                    is TextView -> view.visibility = View.INVISIBLE
                }
            }
        }
    }
}