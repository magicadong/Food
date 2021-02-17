package com.example.food.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.food.data.Repository
import com.example.food.data.local.entity.FavoritesEntity
import com.example.food.data.local.entity.RecipesEntity
import com.example.food.data.model.FoodRecipes
import com.example.food.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {
    /** database */
    // 读取数据库
    var readRecipes:LiveData<List<RecipesEntity>> = repository.localDataSource.readRecipes().asLiveData()
    var readFavorites:LiveData<List<FavoritesEntity>> = repository.localDataSource.readFavorites().asLiveData()

    // 插入数据
    private fun insertRecipes(recipesEntity: RecipesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertRecipe(recipesEntity)
        }
    }

     fun insertFavorite(favoritesEntity: FavoritesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertFavorite(favoritesEntity)
        }
    }

     fun deleteFavorite(favoritesEntity: FavoritesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteFavorite(favoritesEntity)
        }
    }

     fun deleteAllFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteAllFavorites()
        }
    }

    /** retrofit */
    // 食谱数据
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    val searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()

    // 提供外部访问接口
    fun getRecipies(queries: Map<String,String>) = viewModelScope.launch{
        getRecipesSafeCall(queries)
    }

    // 提供外部搜索接口
    fun searchRecipes(searchQueries: Map<String,String>){
        viewModelScope.launch { searchRecipesSafeCall(searchQueries) }
    }

    // 内部具体访问
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remoteDataSource.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                // 如果有数据需要缓存
                val foodRecipe = recipesResponse.value!!.data
                if (foodRecipe != null){
                    offLineCacheRecipes(foodRecipe)
                }
            }catch (e: Exception){
                Log.v("ppp","error: ${e.message}")
                recipesResponse.value = NetworkResult.Error("Food Not Found")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQueries: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remoteDataSource.getRecipes(searchQueries)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)

            }catch (e: Exception){
                searchRecipesResponse.value = NetworkResult.Error("exception:Recipies not found")
            }
        }else{
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private fun offLineCacheRecipes(foodRecipe: FoodRecipes) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    // 对数据进行处理
    private fun handleFoodRecipesResponse(response: Response<FoodRecipes>): NetworkResult<FoodRecipes>? {
        return when{
            response.message().contains("timeout") ->{
                NetworkResult.Error("time out:${response.message()}")
            }
            response.body()!!.results.isNullOrEmpty() ->{
                NetworkResult.Error("empty: Recipes not found")
            }
            response.isSuccessful ->{
                NetworkResult.Success(response.body()!!)
            }
            else ->{
                NetworkResult.Error(response.message())
            }
        }
    }

    // 获取网络连接状态
    fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<Application>()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capability = connectivityManager
                .getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}