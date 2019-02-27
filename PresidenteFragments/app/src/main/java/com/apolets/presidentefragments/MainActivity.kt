package com.apolets.presidentefragments

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity(), ListFragment.ListFragmentListener {
    override fun onPresidentClick(position: Int) {
        val detailFragment = DetailFragment()
        val bundle = Bundle()

        // Send position info to detail view
        bundle.putInt("position",position)
        detailFragment.arguments = bundle
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, detailFragment).addToBackStack(null).commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listFragment = ListFragment()

        fragmentManager.beginTransaction().add(R.id.fragmentContainer, listFragment).commit()


    }
}
