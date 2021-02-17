package com.example.food.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.food.data.model.Result
import com.example.food.util.Constants.Companion.FAVORITES_TABLE_NAME

@Entity(tableName = FAVORITES_TABLE_NAME)
class FavoritesEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var result:Result
)