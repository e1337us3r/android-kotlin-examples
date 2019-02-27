package com.apolets.blegatt

import android.bluetooth.*
import android.content.Context
import android.util.Log
import java.util.UUID
import android.app.Activity
import android.widget.TextView
import android.widget.Toast


class GattCallback(val context: Context) : BluetoothGattCallback() {

    val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
    val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
    val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)
    /* Generates 128-bit UUID from the Protocol Indentifier (16-bit number)
    * and the BASE_UUID (00000000-0000-1000-8000-00805F9B34FB)
    */
    private fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        when (status) {
            BluetoothGatt.GATT_FAILURE -> {
                Log.d("MYAPP", "GATT connection failure")
                return
            }
            BluetoothGatt.GATT_SUCCESS -> {
                Log.d("MYAPP", "GATT connection success")

                (context as Activity).runOnUiThread {
                    Toast.makeText(context,"GATT Connection success",Toast.LENGTH_LONG).show()
                }

            }
        }

        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                Log.d("MYAPP", "Connected GATT service")

                gatt!!.discoverServices()
            }
            BluetoothProfile.STATE_DISCONNECTED -> {
                Log.d("MYAPP", "Disconnected GATT service")
                gatt!!.close()
                (context as Activity).runOnUiThread {
                    Toast.makeText(context,"GATT Connection closed",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)

        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }

        Log.d("MYAPP", "onServicesDiscovered()")

        for (gattService in gatt!!.services) {
            Log.d("MYAPP", "Service ${gattService.uuid}")
            if (gattService.uuid == HEART_RATE_SERVICE_UUID) {
                Log.d("MYAPP", "BINGO!!!")
                for (gattCharacteristic in gattService.characteristics)
                    Log.d("MYAPP", "Characteristic ${gattCharacteristic.uuid}")
                /* setup the system for the notification messages */
                val characteristic = gatt.getService(HEART_RATE_SERVICE_UUID)
                        .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID)
                gatt.setCharacteristicNotification(characteristic, true)


                val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(descriptor)


            }
        }
    }

    override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
        Log.d("MYAPP", "onDescriptorWrite")
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
        Log.d("MYAPP", "Characteristic data received: ${characteristic!!.value}")

        (context as Activity).runOnUiThread {
            val textView = context.findViewById(R.id.txtBLE) as TextView
            textView.text = characteristic.value.toString()
        }

    }


}