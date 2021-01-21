package com.example.food.ui.fragments.detail

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.food.R

class OverViewBinding {
    companion object{
        @BindingAdapter("applyColor")
        @JvmStatic
        fun applyViewColor(view:View, status: Boolean){
            var color = view.context.getColor(R.color.darkGray)
            if (status) {
                 color= view.context.getColor(R.color.green)
            }
            when(view){
                is ImageView -> view.setColorFilter(color)
                is TextView -> view.setTextColor(color)
            }
        }
    }
}