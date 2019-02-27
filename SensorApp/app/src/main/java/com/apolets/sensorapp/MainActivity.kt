package com.apolets.sensorapp

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {


    private var mAzimuth = 0 // degree
    private lateinit var mSensorManager: SensorManager
    private lateinit var mSensor: Sensor

    private val mSensorEventListener = object : SensorEventListener {
        var orientation = FloatArray(3)
        var rMat = FloatArray(9)


        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                // calculate th rotation matrix
                SensorManager.getRotationMatrixFromVector(rMat, event.values)
                // get the azimuth value (orientation[0]) in degree
                mAzimuth = (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[1].toDouble()) + 360).toInt() % 360

                Log.d("MYAPP",mAzimuth.toString())

                runOnUiThread {
                    txtOrientation.text = mAzimuth.toString()
                    txtIsFlat.text = when(mAzimuth){ 0 -> "Flat" else -> "Not Flat"}
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        registerSensorListener()

    }

    fun registerSensorListener(){
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mSensorEventListener)
    }

    override fun onResume() {
        super.onResume()
        registerSensorListener()
    }
}
