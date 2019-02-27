package com.apolets.mulithreadtest


import android.os.Handler
import android.util.Log
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class TxtDownloader(val handler: Handler) : Runnable{


    val urlText = "http://25.io/toau/audio/sample.txt"


    override fun run() {
        val url = URL(urlText)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.doOutput = true

        val inputStream = connection.inputStream
        val txtFromUrl = inputStream.bufferedReader().use ( BufferedReader::readText )
        val stringBuilder = StringBuilder()
        stringBuilder.append(txtFromUrl)
        val stringResult = stringBuilder.toString()
        Log.d("MYAPP",stringResult)

        val msg = handler.obtainMessage()
        msg.what = 0
        msg.obj = stringResult
        handler.sendMessage(msg)


    }
}