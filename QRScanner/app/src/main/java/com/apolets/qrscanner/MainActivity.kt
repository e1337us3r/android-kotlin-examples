package com.apolets.qrscanner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import android.hardware.camera2.CameraCharacteristics
import android.support.v4.content.ContextCompat.getSystemService
import android.hardware.camera2.CameraManager
import android.support.v4.view.ViewCompat.getRotation
import android.hardware.camera2.CameraAccessException
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.R.attr.rotation
import com.google.firebase.ml.vision.common.FirebaseVisionImage




class MainActivity : AppCompatActivity() {

    lateinit var options: FirebaseVisionBarcodeDetectorOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build()


        //val image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation)


    }


    private val ORIENTATIONS = SparseIntArray().also {
        it.append(Surface.ROTATION_0, 90)
        it.append(Surface.ROTATION_90, 0)
        it.append(Surface.ROTATION_180, 270)
        it.append(Surface.ROTATION_270, 180)
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(cameraId: String, activity: Activity, context: Context): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation)

        // On most devices, the sensor orientation is 90 degrees, but for some
        // devices it is 270 degrees. For devices with a sensor orientation of
        // 270, rotate the image an additional 180 ((270 + 270) % 360) degrees.
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        rotationCompensation = (rotationCompensation + sensorOrientation + 270) % 360

        // Return the corresponding FirebaseVisionImageMetadata rotation value.
        val result: Int
        when (rotationCompensation) {
            0 -> result = FirebaseVisionImageMetadata.ROTATION_0
            90 -> result = FirebaseVisionImageMetadata.ROTATION_90
            180 -> result = FirebaseVisionImageMetadata.ROTATION_180
            270 -> result = FirebaseVisionImageMetadata.ROTATION_270
            else -> {
                result = FirebaseVisionImageMetadata.ROTATION_0
                Log.e("MYAPP", "Bad rotation value: $rotationCompensation")
            }
        }
        return result
    }


}
