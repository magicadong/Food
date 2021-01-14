package com.example.food.util

/**
 * 密封类用于限制类的继承结构：当一个值为有限几种的类型、而不能有任何其他类型时
 */
sealed class NetworkResult<T>(
        val data: T? = null,
        val message: String? = null
){
        class Success<T>(data: T):NetworkResult<T>(data)

        class Error<T>(errMsg: String):NetworkResult<T>(message = errMsg)

        class Loading<T>():NetworkResult<T>()
}

