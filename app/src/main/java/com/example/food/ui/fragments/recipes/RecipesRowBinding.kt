package com.example.food.ui.fragments.recipes

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.food.R
import com.example.food.data.model.Result
import com.example.food.ui.fragments.recipes.RecipesFragmentDirections
import com.example.food.util.Constants.Companion.IMAGE_BASE_URL
import org.jsoup.Jsoup

class RecipesRowBinding {
    companion object{
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view:View, vegan:Boolean){
            if (vegan){
                when(view){
                    is TextView -> view.setTextColor(
                            view.context.getColor(R.color.green)
                    )
                    is ImageView -> view.setColorFilter(
                            view.context.getColor(R.color.green)
                    )
                }
            }else{
                when(view){
                    is TextView -> view.setTextColor(
                            view.context.getColor(R.color.darkGray)
                    )
                    is ImageView -> view.setColorFilter(
                            view.context.getColor(R.color.darkGray)
                    )
                }
            }
        }

        @BindingAdapter("loadImageUrl")
        @JvmStatic
        fun loadImageUrl(imageView: ImageView, url:String){
            imageView.load(url){
                crossfade(600)
                placeholder(R.drawable.error_placeholder)
            }
        }

        @BindingAdapter("loadIngredientImageUrl")
        @JvmStatic
        fun loadIngredientImageUrl(imageView: ImageView, url:String){
            imageView.load(IMAGE_BASE_URL + url){
                crossfade(600)
                placeholder(R.drawable.error_placeholder)
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, text: String){
            textView.text = Jsoup.parse(text).text()
        }

        @BindingAdapter("showDetail")
        @JvmStatic
        fun showDetail(layout: ConstraintLayout, result: Result){
            layout.setOnClickListener {
                val action = RecipesFragmentDirections
                    .actionRecipesFragmentToDetailActivity(result)
                layout.findNavController().navigate(action)
            }
        }
    }
}