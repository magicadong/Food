package com.example.food.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.food.data.DataStoreRepository
import com.example.food.util.Constants
import com.example.food.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.food.util.Constants.Companion.DEFAULT_MEAL_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(
        application: Application,
        private val dataStoreRepository: DataStoreRepository
) :AndroidViewModel(application) {
    var mealType = DEFAULT_MEAL_TYPE
    var dietType = DEFAULT_DIET_TYPE

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    var networkStatus = false
        set(value) {
            field = value
            showNetworkStatus()
        }

    var backOnline = false
    val readBackOnline = dataStoreRepository.backOnline.asLiveData()

    fun showNetworkStatus() {
        if (!networkStatus){
            Toast.makeText(getApplication(),"No Internet Connection",Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else{
            if (backOnline){
                Toast.makeText(getApplication(),"Back Online",Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

    fun saveBackOnline(status: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveBackOnline(status)
        }
    }

    fun saveMealAndDiet(mealType:String,mealId:Int,dietType:String,dietId:Int){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveMealAndDiet(mealType,mealId,dietType, dietId)
        }
    }

    // 构造请求参数
    //number=1&apiKey=e0401bc1cfc44b649d3e479cf73d4dca
    // &type=breakfast&diet=vegan&addRecipeInformation=true
    // &fillIngredients=true
    fun applyQueries():HashMap<String,String>{
        viewModelScope.launch {
            readMealAndDietType.collect {
                mealType = it.mealType
                dietType = it.dietType
            }
        }

        val queries:HashMap<String,String> = HashMap()
        queries[Constants.QUERY_NUMBER] = "50"
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun applySearchQueries(search: String):HashMap<String,String>{
        val queries:HashMap<String,String> = HashMap()
        queries[Constants.QUERY_NUMBER] = "50"
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_QUERY] = search
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }
}