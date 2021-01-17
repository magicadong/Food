package com.example.food.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.food.data.Repository
import com.example.food.data.local.RecipesEntity
import com.example.food.data.model.FoodRecipes
import com.example.food.util.Constants.Companion.API_KEY
import com.example.food.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.food.util.Constants.Companion.QUERY_API_KEY
import com.example.food.util.Constants.Companion.QUERY_DIET
import com.example.food.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.food.util.Constants.Companion.QUERY_NUMBER
import com.example.food.util.Constants.Companion.QUERY_TYPE
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

    // 插入数据
    private fun insertRecipes(recipesEntity: RecipesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertRecipe(recipesEntity)
        }
    }

    /** retrofit */
    // 食谱数据
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()

    // 提供外部访问接口
    fun getRecipies(queries: Map<String,String>) = viewModelScope.launch{
        getRecipesSafeCall(queries)
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
                recipesResponse.value = NetworkResult.Error("exception:Recipies not found")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
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
                NetworkResult.Error("time out")
            }
            response.code() == 402 ->{
                NetworkResult.Error("appkey limitted")
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