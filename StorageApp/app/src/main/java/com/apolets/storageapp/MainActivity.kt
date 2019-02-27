package com.apolets.storageapp

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnRead.setOnClickListener{
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,ReadFragment()).commit()
        }

        btnWrite.setOnClickListener{
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,WriteFragment()).commit()
        }

    }
}
