package com.apolets.graphapp

import android.app.Activity
import android.os.Bundle
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : Activity() {

    val dataPoints = ArrayList<DataPoint>()
    val random = Random()
    var currentX = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateData()

    }

    fun ClosedRange<Int>.random() =
            (Random().nextInt((endInclusive + 1) - start) + start).toDouble()

    fun updateData() {

        Thread(Runnable {


            while (true) {


                dataPoints.add(DataPoint((++currentX).toDouble(), (0..10).random()))

                runOnUiThread {
                    graph.addSeries(LineGraphSeries<DataPoint>(dataPoints.toTypedArray()))
                }


                Thread.sleep(1000)
            }
        }).start()

    }


}
