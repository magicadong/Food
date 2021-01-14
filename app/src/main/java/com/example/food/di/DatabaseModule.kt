package com.example.food.di

import com.example.food.data.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideLocalInstance(): LocalDataSource{
        return LocalDataSource()
    }
}