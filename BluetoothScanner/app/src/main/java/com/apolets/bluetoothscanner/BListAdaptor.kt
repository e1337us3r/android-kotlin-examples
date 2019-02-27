package com.apolets.bluetoothscanner

import android.bluetooth.le.ScanResult
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.Random

class BListAdaptor(val context : Context,val devicesList: List<ScanResult>) : BaseAdapter(){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
   private val random = Random()
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

       val itemView = if(convertView == null) inflater.inflate(R.layout.bl_item,parent,false) else convertView

        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtMac = itemView.findViewById<TextView>(R.id.txtMAC)
        val txtSignal = itemView.findViewById<TextView>(R.id.txtSignal)

        txtName.text = if(getItem(position).device.name == null) "No Name" else getItem(position).device.name
        txtMac.text = getItem(position).device.address
        txtSignal.text = getItem(position).rssi.toString()

        itemView.isEnabled = isEnabled(position)

        return itemView
    }

    override fun getItem(p0: Int): ScanResult {
        return devicesList[p0]
    }

    override fun isEnabled(p0: Int): Boolean {
        return random.nextBoolean()
    }

    override fun getCount(): Int {
        return devicesList.size
    }


}