package com.apolets.accessible

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    companion object {
        var count: Int = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCount.text = count.toString()

        fab.setOnClickListener({
            println("Click!")
            count = txtCount.text.toString().toInt() + 1
            txtCount.text = count.toString()
        })
    }
}
