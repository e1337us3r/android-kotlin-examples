package com.apolets.bluetoothscanner

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private var scanResultList: HashMap<String, ScanResult> = HashMap()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var scanCallback : ScanCallback? = null
    private var handler : Handler? = null
    private var bluetoothLeScanner : BluetoothLeScanner? = null
    private var isScanning = false

    companion object {
        const val SCAN_PERIOD: Long = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter


        listView.adapter = BListAdaptor(this, scanResultList.values.toList())


        if (hasPermissions()) btnScan.setOnClickListener { startScan() }
    }



    private fun startScan(){
        Log.d("MYAPP","Scanning...")

        scanResultList = HashMap()

        scanCallback = BtleScanCallback()
        bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner

        val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build()

        val filter: List<ScanFilter>? = null

        //Stops scanning after SCAN_PERIOD
        handler = Handler()
        handler!!.postDelayed({stopScan()}, SCAN_PERIOD)

        isScanning = true
        bluetoothLeScanner!!.startScan(filter,settings,scanCallback)


    }


    private fun stopScan(){

        bluetoothLeScanner!!.stopScan(scanCallback)
        scanCallback = null
        handler = null
        isScanning = false

        Log.d("MYAPP","Scan complete.")

        listView.adapter = BListAdaptor(this, scanResultList.values.toList())

    }


    private inner class BtleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)

        }
        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }
        override fun onScanFailed(errorCode: Int) {
            Log.d("DBG", "BLE Scan Failed with code $errorCode")
        }
        private fun addScanResult(result: ScanResult) {

            val device = result.device
            val deviceAddress = device.address
            scanResultList[deviceAddress] = result
            Log.d("DBG", "Device address: $deviceAddress, power: ${result.rssi}, name: ${result.device.name}")
        }
    }

    private fun hasPermissions(): Boolean {
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Log.d("DBG", "No Bluetooth LE capability")
            return false
        } else if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d("DBG", "No fine location access")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1);
            return true // assuming that the user grants permission
        }
        return true
    }

}
