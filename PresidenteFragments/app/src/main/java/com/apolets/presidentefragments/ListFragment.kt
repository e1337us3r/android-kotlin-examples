package com.apolets.presidentefragments

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.apolets.presidente.GlobalModel
import com.apolets.presidente.PresidentAdapter


class ListFragment() : Fragment() {
    internal var activityCallBack: ListFragmentListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val masterView = inflater.inflate(R.layout.fragment_list, container, false)
        val inflatedList = masterView.findViewById<ListView>(R.id.listView)

        // Set the adapter for our ListView object
        inflatedList.adapter = PresidentAdapter(context, GlobalModel.presidents)

        // Call the onPresidentClick function that is implemented in parent activity
        inflatedList.setOnItemClickListener{_,_,position,_ ->
            activityCallBack!!.onPresidentClick(position)
        }

        return masterView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // get implemented interface reference from parent activity?
        activityCallBack = context as ListFragmentListener


    }
    interface ListFragmentListener {
        fun onPresidentClick(position: Int)
    }

}
