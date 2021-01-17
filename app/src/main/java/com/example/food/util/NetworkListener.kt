package com.example.food.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@ExperimentalCoroutinesApi
class NetworkListener: ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow<Boolean>(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean>{
        val manager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerDefaultNetworkCallback(this)

        var isConnect = false
        manager.allNetworks.forEach { network ->
            val capability = manager.getNetworkCapabilities(network)
            if (capability != null){
                if (capability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                    isConnect = true
                    return@forEach
                }
            }
        }
        isNetworkAvailable.value = isConnect
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}