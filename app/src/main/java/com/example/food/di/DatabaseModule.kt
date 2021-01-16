package com.example.food.di

import android.content.Context
import androidx.room.Room
import com.example.food.data.local.RecipesDao
import com.example.food.data.local.RecipesDatabase
import com.example.food.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule{
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):RecipesDatabase{
        return Room.databaseBuilder(
                context,
                RecipesDatabase::class.java,
                DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideRecipesDao(database: RecipesDatabase): RecipesDao {
        return database.recipesDao()
    }
}