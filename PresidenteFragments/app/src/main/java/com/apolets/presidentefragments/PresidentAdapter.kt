package com.apolets.presidente

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.apolets.presidentefragments.R

class PresidentAdapter(val context : Context,val presidents: MutableList<President>): BaseAdapter(){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = inflater.inflate(R.layout.item_president,parent,false)

        val currentItem = presidents[position]

        val txtName = itemView.findViewById<TextView>(R.id.txtPName)
        val txtEndDate = itemView.findViewById<TextView>(R.id.txtEnd)
        val txtStartDate = itemView.findViewById<TextView>(R.id.txtStart)

        txtName.text = currentItem.name
        txtEndDate.text = currentItem.endDuty.toString()
        txtStartDate.text = currentItem.startDuty.toString()

        return itemView

    }

    override fun getItem(position: Int): Any {
        return presidents[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return presidents.size
    }

}