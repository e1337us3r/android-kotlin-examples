package com.apolets.presidenteretrofit

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.net.Uri
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder


class MainActivity : Activity() {

    val wikiDetailsURL = "https://fi.wikipedia.org?search="


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
            getHitCount(position)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->

            Log.d("MYAPP", "Long Click!")

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(wikiDetailsURL + URLEncoder.encode(GlobalModel.presidents[position].name, "UTF-8"))
            startActivity(intent)

            true
        }
    }

    fun getHitCount(position: Int){

        val search = WikiAPI.service.search(GlobalModel.presidents[position].name)

        val settings = object : Callback<WikiAPI.Model.Query>{
            override fun onFailure(call: Call<WikiAPI.Model.Query>, t: Throwable) {
                Log.e("MYAPP", t.toString())
            }

            override fun onResponse(call: Call<WikiAPI.Model.Query>, response: Response<WikiAPI.Model.Query>) {

                val resBody : WikiAPI.Model.Query = response.body() !!

                txtCount.text = resBody.query.searchinfo.totalhits.toString()

            }


        }

        search.enqueue(settings)


    }


}
