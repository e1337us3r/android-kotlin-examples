package com.apolets.presidente

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import java.net.URLEncoder


class MainActivity : Activity() {

    val wikiURL = "https://fi.wikipedia.org?search="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView.adapter = PresidentAdapter(this, GlobalModel.presidents)


        setUpListeners()

    }

    fun setUpListeners() {
        listView.setOnItemClickListener { _, _, position, _ ->
            Log.d("MYAPP", "Click! $position")
            val selectedPresident = GlobalModel.presidents[position]
            txtName.text = "${selectedPresident.name}  (${selectedPresident.startDuty} - ${selectedPresident.endDuty})"
            txtDesc.text = selectedPresident.description
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->

            Log.d("MYAPP", "Long Click!")

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(wikiURL + URLEncoder.encode(GlobalModel.presidents[position].name, "UTF-8"))
            startActivity(intent)

            true
        }
    }
}
