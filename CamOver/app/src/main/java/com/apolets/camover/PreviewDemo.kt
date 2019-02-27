package com.apolets.camover

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.hardware.Camera
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.preview.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


class PreviewDemo : Activity(), View.OnClickListener {

    private var preview: SurfaceView? = null
    private var previewHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var inPreview = false
    internal var bmp: Bitmap? = null
    internal var itembmp: Bitmap? = null
    internal var start = PointF()
    internal var mid = PointF()
    internal var oldDist = 1f
    internal var imageFileName: File? = null
    internal var imageFileFolder: File? = null
    private var msConn: MediaScannerConnection? = null
    internal var d: Display? = null
    internal var screenhgt: Int = 0
    internal var screenwdh: Int = 0
    internal lateinit var dialog: ProgressDialog

    internal var surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                camera!!.setPreviewDisplay(previewHolder)
            } catch (t: Throwable) {
                Log.e("surfaceCallback",
                        "Exception in setPreviewDisplay()", t)
                Toast.makeText(this@PreviewDemo, t.message, Toast.LENGTH_LONG)
                        .show()
            }

        }

        override fun surfaceChanged(holder: SurfaceHolder,
                                    format: Int, width: Int,
                                    height: Int) {
            val parameters = camera!!.parameters
            val size = getBestPreviewSize(width, height,
                    parameters)

            if (size != null) {
                parameters.setPreviewSize(size.width, size.height)
                camera!!.parameters = parameters
                camera!!.startPreview()
                inPreview = true
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // no-op
        }
    }


    internal var photoCallback: Camera.PictureCallback = Camera.PictureCallback { data, camera ->
        dialog = ProgressDialog.show(this@PreviewDemo, "", "Saving Photo")
        object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {
                }

                onPictureTake(data, camera)
            }
        }.start()
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
            }
        }


        preview = findViewById<View>(R.id.surface) as SurfaceView?

        previewHolder = preview!!.holder
        previewHolder!!.addCallback(surfaceCallback)
        previewHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        previewHolder!!.setFixedSize(window.windowManager
                .defaultDisplay.width, window.windowManager
                .defaultDisplay.height)

        var xCoOrdinate = 0f
        var yCoOrdinate = 0f

        imageView.setOnTouchListener(object:View.OnTouchListener {
            override fun onTouch(view:View, event:MotionEvent):Boolean {
                when (event.getActionMasked()) {
                    MotionEvent.ACTION_DOWN -> {
                        xCoOrdinate = view.getX() - event.getRawX()
                        yCoOrdinate = view.getY() - event.getRawY()
                    }
                    MotionEvent.ACTION_MOVE -> view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start()
                    else -> return false
                }
                return true
            }
        })


    }


    public override fun onResume() {
        super.onResume()
        camera = Camera.open()
    }

    public override fun onPause() {
        if (inPreview) {
            camera!!.stopPreview()
        }

        camera!!.release()
        camera = null
        inPreview = false
        super.onPause()
    }

    private fun getBestPreviewSize(width: Int, height: Int, parameters: Camera.Parameters): Camera.Size? {
        var result: Camera.Size? = null
        for (size in parameters.supportedPreviewSizes) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size
                } else {
                    val resultArea = result.width * result.height
                    val newArea = size.width * size.height
                    if (newArea > resultArea) {
                        result = size
                    }
                }
            }
        }
        return result
    }


    fun onPictureTake(data: ByteArray, camera: Camera) {

        bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
        mutableBitmap = bmp!!.copy(Bitmap.Config.ARGB_8888, true)
        savePhoto(mutableBitmap)
        dialog.dismiss()
    }


    internal inner class SavePhotoTask : AsyncTask<ByteArray, String, String>() {
        override fun doInBackground(vararg jpeg: ByteArray): String? {
            val photo = File(Environment.getExternalStorageDirectory(), "photo.jpg")
            if (photo.exists()) {
                photo.delete()
            }
            try {
                val fos = FileOutputStream(photo.path)
                fos.write(jpeg[0])
                fos.close()
            } catch (e: java.io.IOException) {
                Log.e("PictureDemo", "Exception in photoCallback", e)
            }

            return null
        }
    }


    fun savePhoto(bmp: Bitmap) {
        imageFileFolder = File(Environment.getExternalStorageDirectory(), "Rotate")
        imageFileFolder!!.mkdir()
        var out: FileOutputStream? = null
        val c = Calendar.getInstance()
        val date = fromInt(c.get(Calendar.MONTH)) + fromInt(c.get(Calendar.DAY_OF_MONTH)) + fromInt(c.get(Calendar.YEAR)) + fromInt(c.get(Calendar.HOUR_OF_DAY)) + fromInt(c.get(Calendar.MINUTE)) + fromInt(c.get(Calendar.SECOND))
        imageFileName = File(imageFileFolder, "$date.jpg")
        try {
            out = FileOutputStream(imageFileName)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            scanPhoto(imageFileName!!.toString())
            out = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun fromInt(`val`: Int): String {
        return `val`.toString()
    }

    fun scanPhoto(imageFileName: String) {
        msConn = MediaScannerConnection(this@PreviewDemo, object : MediaScannerConnectionClient {
            override fun onMediaScannerConnected() {
                msConn!!.scanFile(imageFileName, null)
                Log.i("Photo Utility", "connection established")
            }

            override fun onScanCompleted(path: String, uri: Uri) {
                msConn!!.disconnect()
                Log.i("Photo Utility", "scan completed")
            }
        })
        msConn!!.connect()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.repeatCount == 0) {
            onBack()
        }
        return super.onKeyDown(keyCode, event)
    }

    fun onBack() {
        Log.e("onBack :", "yes")
        camera!!.takePicture(null, null, photoCallback)
        inPreview = false
    }

    override fun onClick(v: View) {

    }

    companion object {
        internal lateinit var mutableBitmap: Bitmap
    }

}