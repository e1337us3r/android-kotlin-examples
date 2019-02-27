package com.apolets.mycam

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.net.URI
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.R.attr.path



class MainActivity : AppCompatActivity() {

    companion object {
        private var mImageUri: Uri? = null
    }


    private val REQUEST_IMAGE_CAPTURE = 1
    private var mCurrentPhotoPath: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create a file into app's external storage
        val fileName = "temp_photo"
        val imgPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(fileName, ".jpg", imgPath)


        //Create Content URI
        mImageUri = FileProvider.getUriForFile(this,
                "com.apolets.mycam.fileprovider",
                imageFile)


        mCurrentPhotoPath = imageFile!!.absolutePath

        btnShoot.setOnClickListener {

            val myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (myIntent.resolveActivity(packageManager) != null) {
                myIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                startActivityForResult(myIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d("MYAPP","uri: $mImageUri")

            val mIntent = Intent(Intent.ACTION_VIEW)
            mIntent.setDataAndType(mImageUri, "image/*")
            mIntent.flags = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION //must for reading data from directory
            startActivity(mIntent)
        }
    }


}

