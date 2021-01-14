package com.example.food.data

import com.example.food.data.local.LocalDataSource
import com.example.food.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
        val remoteDataSource: RemoteDataSource,
        val localDataSource: LocalDataSource
) {

}