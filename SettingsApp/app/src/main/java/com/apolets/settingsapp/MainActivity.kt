package com.apolets.settingsapp

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.*
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {

        Log.d("MYAPP","Change")

        val color = p0?.getString(getString(R.string.list_preference_color),"N/A")
        Log.d("MYAPP",color)

        when (color) {
            "Red" -> textView.setBackgroundColor(resources.getColor(R.color.Red))
            "Green" -> textView.setBackgroundColor(resources.getColor(R.color.Green))
            "Blue" -> textView.setBackgroundColor(resources.getColor(R.color.Blue))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSettings.setOnClickListener {
            fragmentManager.beginTransaction().replace(android.R.id.content,
                    SettingsFragment()).commit()
            /*val sp = PreferenceManager.getDefaultSharedPreferences(this)
            textView.text = sp.getString(getString(R.string.list_preference_color),
                    "N/A")*/
        }
        val sp = PreferenceManager.getDefaultSharedPreferences(this)

        sp.registerOnSharedPreferenceChangeListener(this)
    }
}
