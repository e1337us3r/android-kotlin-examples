package com.apolets.presidentefragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apolets.presidente.GlobalModel

class DetailFragment() : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val masterView = inflater.inflate(R.layout.detail_layout, container, false)

        //Get text views
        val txtName = masterView.findViewById<TextView>(R.id.txtPName)
        val txtDet = masterView.findViewById<TextView>(R.id.txtPDetails)

        // Set correct values for views, we get the info from the activity that created this fragment
        txtName.text = GlobalModel.presidents[arguments["position"] as Int].name
        txtDet.text = GlobalModel.presidents[arguments["position"] as Int].description

        return masterView
    }

}