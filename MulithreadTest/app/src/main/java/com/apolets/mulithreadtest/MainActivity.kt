package com.apolets.mulithreadtest

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val messageHandler: Handler = object :
        Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            if (msg.what == 0){
                txtView.text = msg.obj.toString()
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isNetworkAvailable()) {
            val myRunnable = TxtDownloader(messageHandler)
            val myThread = Thread(myRunnable)
            myThread.start()
        }


    }

    fun isNetworkAvailable(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        if (isConnected) {
            return true
        }
        return false
    }
}
