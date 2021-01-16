package com.example.food.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.food.R
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
                error(R.drawable.error_placeholder)
                placeholder(R.drawable.error_placeholder)
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, text: String){
            textView.text = Jsoup.parse(text).text()
        }
    }
}