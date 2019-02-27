package com.apolets.multithreadimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val imageUrl = "https://toppng.com/public/uploads/preview/crown-png-pic-11526479547za2slstgvg.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            GetImage().execute(URL(imageUrl))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    inner class GetImage : AsyncTask<URL, Unit, Bitmap>() {
        override fun doInBackground(vararg url: URL?): Bitmap? {
            val image: Bitmap

            try {


                val connection = url[0]!!.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.doOutput = true

                val inputStream = connection.inputStream

                image = BitmapFactory.decodeStream(inputStream)

                inputStream.close()
                return image
            }catch (e:Exception){
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }

    }


}
