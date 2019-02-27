package com.apolets.testacceleration

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mSensor: Sensor


    private val mSensorEventListener = object : SensorEventListener {


        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                Log.d("MYAPP","x-axis = ${event.values[0]} y-axis = ${event.values[1]} z-axis = ${event.values[2]}")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)


        registerSensorListener()

    }

    fun registerSensorListener(){
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI)
    }
}
