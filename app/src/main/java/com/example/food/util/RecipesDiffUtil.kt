package com.example.food.util

import androidx.recyclerview.widget.DiffUtil
import com.example.food.data.model.Result

class RecipesDiffUtil<T>(
        private val oldData: List<T>,
        private val newData: List<T>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] === newData[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}