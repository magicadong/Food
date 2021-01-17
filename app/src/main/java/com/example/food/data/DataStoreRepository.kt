package com.example.food.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.*
import com.example.food.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.food.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.food.util.Constants.Companion.DIET_ID_KEY
import com.example.food.util.Constants.Companion.DIET_TYPE_KEY
import com.example.food.util.Constants.Companion.MEAL_ID_KEY
import com.example.food.util.Constants.Companion.MEAL_TYPE_KEY
import com.example.food.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
        @ApplicationContext private val context: Context) {

    companion object{
        val mealTypeKey = preferencesKey<String>(MEAL_TYPE_KEY)
        val mealIdKey = preferencesKey<Int>(MEAL_ID_KEY)
        val dietTypeKey = preferencesKey<String>(DIET_TYPE_KEY)
        val dietIdKey = preferencesKey<Int>(DIET_ID_KEY)
    }
    private val dataStore: DataStore<Preferences> = context.createDataStore(
            PREFERENCES_NAME
    )

    //存储数据
    suspend fun saveMealAndDiet(
            mealType:String,
            mealId:Int,
            dietType:String,
            dietId:Int){
        dataStore.edit { preferences ->
            preferences[mealTypeKey] = mealType
            preferences[mealIdKey] = mealId
            preferences[dietTypeKey] = dietType
            preferences[dietIdKey] = dietId
        }
    }

    // 使用flow读取数据
    val readMealAndDietType:Flow<MealInfo> = dataStore.data
            .catch { exception ->
                if (exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map {preferences ->
                MealInfo(
                        preferences[mealTypeKey] ?: DEFAULT_MEAL_TYPE,
                        preferences[mealIdKey] ?: 0,
                        preferences[dietTypeKey] ?: DEFAULT_DIET_TYPE,
                        preferences[dietIdKey] ?: 0
                )
            }
}

data class MealInfo(
        val mealType: String,
        val mealId: Int,
        val dietType: String,
        val dietId: Int
)