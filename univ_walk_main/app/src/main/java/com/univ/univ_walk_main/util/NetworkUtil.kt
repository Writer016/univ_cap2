package com.univ.univ_walk_main.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkUtil(context: Context) {
    private val thisNetworkState: MutableLiveData<Boolean> = MutableLiveData() //Livedata to check network state
    val networkState: LiveData<Boolean> //livedate 자료형으로 선언한 networkState를 메인에서 관찰.
        get() = thisNetworkState //livedata 값은 수정 불가하니 getter로 Mutablelivedata를 받도록.

    private val connectivityManager: ConnectivityManager

    //https://developer.android.com/develop/connectivity/network-ops/reading-network-state?hl=ko
    private val networkCallBack = object : ConnectivityManager.NetworkCallback(){ //network 상태
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            thisNetworkState.postValue(true)
        }
        override fun onLost(network: Network) {
            super.onLost(network)
            thisNetworkState.postValue(false)
        }
    }

    init{
        connectivityManager = context.getSystemService(ConnectivityManager::class.java) //manage network state
    }

    fun registerNetwork(){
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallBack) //register networkCallback
    }

    fun unRegisterNetwork(){
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }

}