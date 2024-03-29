package com.univ.kotlin_walk2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager: SensorManager
    lateinit var counterSensor: Sensor
    var steps = 0
    var register = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)!=PackageManager.PERMISSION_GRANTED){ //미승인 시
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 100) //request permission
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        counterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!

        if(counterSensor == null){
            Toast.makeText(this, "its null", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume(){
        super.onResume()
        if(counterSensor != null) {
            sensorManager.registerListener(
                this,
                counterSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
            register = true
        }
    }

    override fun onPause() {
        super.onPause()
        if(counterSensor != null) {
            sensorManager.unregisterListener(this)
        }
    }

    //걸음 수 데이터 갱신
    override fun onSensorChanged(event: SensorEvent){
        if(counterSensor != null && !register){
            steps++
        } else{
            register = false
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}