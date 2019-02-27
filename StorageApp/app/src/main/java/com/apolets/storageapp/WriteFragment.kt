package com.apolets.storageapp

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_read.*
import kotlinx.android.synthetic.main.fragment_write.*

const val FILENAME = "myFile.txt"

class WriteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnSave.setOnClickListener {
            activity.openFileOutput(FILENAME,
                    android.content.Context.MODE_APPEND).bufferedWriter().use {
                it.write("\n")
                it.write(editText.text.toString())
            }

            txtResult.text = "Write successful"
        }
    }


}
