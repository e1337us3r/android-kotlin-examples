package com.apolets.blerapperapp

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.util.Log
import java.util.*

class MyBLECallback: BleWrapper.BleCallback {

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

    override fun onDeviceReady(gatt: BluetoothGatt) {
        Log.d("MYAPP","Device ready.")


        for (gattService in gatt.services) {
            Log.d("DBG", "Service ${gattService.uuid}")
            if (gattService.uuid == HEART_RATE_SERVICE_UUID) {
                Log.d("DBG", "BINGO!!!")
                for (gattCharacteristic in gattService.characteristics)
                    Log.d("DBG", "Characteristic ${gattCharacteristic.uuid}")
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

    override fun onDeviceDisconnected() {
        Log.d("MYAPP","Device disconnected.")

    }

    override fun onNotify(characteristic: BluetoothGattCharacteristic) {
        Log.d("MYAPP","On notify.")
        Log.d("MYAPP", "Characteristic data received: ${characteristic.value}")

    }
}