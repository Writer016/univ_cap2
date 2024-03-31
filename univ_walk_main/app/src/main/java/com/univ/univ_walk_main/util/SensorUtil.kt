package com.univ.univ_walk_main.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SensorUtil(val activity: Activity) {

    fun requestPermission(){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 200) //request permission
    } //센서를 사용하기 위한 권한 요청

    fun checkPermission(): Boolean{
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED
    }
}