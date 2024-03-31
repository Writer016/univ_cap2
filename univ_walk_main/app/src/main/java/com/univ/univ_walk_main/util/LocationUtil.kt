package com.univ.univ_walk_main.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LocationUtil(val context: Context) {
    val locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val thisLocState: MutableLiveData<Boolean> = MutableLiveData() //Livedata to check call
    val locState: LiveData<Boolean>
        get() = thisLocState

    fun getLatitude() = latitude
    fun getLongitude() = longitude

    fun getLocation(){
        getLocationWithListener()
        getLocationWithProvider()
    }

    fun checkPermission(): Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100) //request permission
    }

    fun checkGPS(): Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //프로바이더 사용 가능한지
    }

    private fun getLocationWithListener(){
        if(checkPermission()) {
            val locList = object : LocationListener {
                override fun onLocationChanged(p0: Location) { //위치 바뀌면.. 위치 정보는 p0가 가지고 있다
                    latitude = p0.latitude
                    longitude = p0.longitude
                    thisLocState.postValue(true)
                    locationManager.removeUpdates(this) //해제
                }
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locList) //gps를 통해 위치정보 가져오기
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locList)
        }
    }

    private fun getLocationWithProvider(): Boolean{
        var tf = false
        if(checkPermission()) {
            var currentLocation: Location? = null
            for (provider in locationManager.getProviders(true)) { //위치제공자 가져오기
                val loc = locationManager.getLastKnownLocation(provider) //위치정보 얻기
                if (loc == null) continue //위치값 얻지 못했으면

                //위치값 얻었으면
                if (currentLocation == null || loc.accuracy < currentLocation.accuracy) { //최근 위치가 null이거나 정확도가 높으면
                    currentLocation = loc
                }
            }
            if (currentLocation != null) {
                latitude = currentLocation.latitude //위도
                longitude = currentLocation.longitude //경도
                tf=true
                thisLocState.postValue(true)
            }
        }

        return tf
    }
}