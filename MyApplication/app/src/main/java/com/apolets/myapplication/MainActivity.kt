package com.apolets.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    companion object {
        var hello_check: Boolean = false
        var currentText: String = "Hello World!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtHello.text = currentText

        button.setOnClickListener {
            if (hello_check) {
                currentText = "Goodbye Summer"
                hello_check = false
            } else {
                currentText = "Hello World!"
                hello_check = true
            }

            txtHello.text = currentText
            Log.d("MyApp", "Click!")

        }



        Log.d("MyApp", "App started")
    }
}
