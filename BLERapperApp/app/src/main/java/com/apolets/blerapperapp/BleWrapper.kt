package com.apolets.blerapperapp

/*
 * BLE Wrapper -- Bluetooth LE message based implementation
 *
 * Based on concepts presented at
 * https://hellsoft.se/bluetooth-low-energy-on-android-part-1-1aa8bf60717d
 *
 * Written by Jarkko Vuori 2018
 */

import android.bluetooth.*
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import java.util.*

class BleWrapper(private val context: Context, deviceAddress: String) : Handler.Callback {

    private val bluetoothDevice: BluetoothDevice
    private val mainHandler = Handler(Looper.getMainLooper(), this)
    private val bleHandler: Handler
    private val myBleCallback = MyBleCallback()

    private val listeners = HashSet<BleCallback>()

    interface BleCallback {
        /**
         * Signals that the BLE device is ready for communication.
         */
        fun onDeviceReady(gatt: BluetoothGatt)

        /**
         * Signals that a connection to the device was lost.
         */
        fun onDeviceDisconnected()

        /**
         * Signals that notify was received.
         */
        fun onNotify(characteristic: BluetoothGattCharacteristic)
    }

    init {
        val handlerThread = HandlerThread("BleThread")
        handlerThread.start()
        bleHandler = Handler(handlerThread.looper, this)
        bluetoothDevice = getBluetoothDevice(context, deviceAddress)
    }

    private fun getBluetoothDevice(context: Context, deviceAddress: String): BluetoothDevice {
        val bluetoothManager = context
                .getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        return bluetoothAdapter.getRemoteDevice(deviceAddress)
    }

    /*
     * These functions you can call from your application
     */
    fun addListener(bleCallback: BleCallback) {
        listeners.add(bleCallback)
    }

    fun removeListener(bleCallback: BleCallback) {
        listeners.remove(bleCallback)
    }

    fun connect(autoConncet: Boolean) {
        bleHandler.obtainMessage(MSG_CONNECT, autoConncet).sendToTarget()
    }

    fun disconnect(gatt: BluetoothGatt) {
        bleHandler.obtainMessage(MSG_DISCONNECT, gatt).sendToTarget()
    }

    fun getNotifications(gatt: BluetoothGatt, service: UUID, characteristic: UUID ) {
        bleHandler.obtainMessage(MSG_GET_NOTIFICATIONS, ServiceCharacteristic(gatt, service, characteristic)).sendToTarget()
    }
    /**************************************/

    override fun handleMessage(message: Message): Boolean {
        Log.d("DBG", "BleWrapper: handleMessage ${message.what}")
        when (message.what) {
            MSG_CONNECT -> doConnect(message.obj as Boolean)
            MSG_CONNECTED -> (message.obj as BluetoothGatt).discoverServices()
            MSG_DISCONNECT -> (message.obj as BluetoothGatt).disconnect()
            MSG_DISCONNECTED -> {(message.obj as BluetoothGatt).close()
                                  mainHandler.obtainMessage(MSG_CLOSED).sendToTarget()
                                }
            MSG_GET_NOTIFICATIONS -> doRequestNotifications(message.obj as ServiceCharacteristic)
            MSG_NOTIFY -> doNotifyNotifies(message.obj as BluetoothGattCharacteristic)
            MSG_SERVICES_DISCOVERED -> doNotifyReady(message.obj as BluetoothGatt)
            MSG_CLOSED -> doNotifyClosed()
        }
        return true
    }

    private fun doNotifyReady(gatt: BluetoothGatt) {
        for (listener in listeners) {
            listener.onDeviceReady(gatt)
        }
    }

    private fun doNotifyNotifies(characteristic: BluetoothGattCharacteristic) {
        for (listener in listeners) {
            listener.onNotify(characteristic)
        }
    }

    private fun doNotifyClosed() {
        for (listener in listeners) {
            listener.onDeviceDisconnected()
        }
    }

    private fun doConnect(autoConnect: Boolean) {
        bluetoothDevice.connectGatt(context, autoConnect, myBleCallback)
    }

    private fun doRequestNotifications(servchr: ServiceCharacteristic) {
        // setup the system for the notification messages
        val characteristic = servchr.gatt.getService(servchr.service)
                .getCharacteristic(servchr.characteristic)
        servchr.gatt.setCharacteristicNotification(characteristic, true)

        // then enable them on the server
        val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        servchr.gatt.writeDescriptor(descriptor)
    }

    private inner class MyBleCallback : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.d("DBG", "onConnectionStateChange $status")
            // http://allmydroids.blogspot.com/2015/06/android-ble-error-status-codes-explained.html
            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (newState) {
                    BluetoothGatt.STATE_CONNECTED -> bleHandler.obtainMessage(MSG_CONNECTED, gatt).sendToTarget()
                    BluetoothGatt.STATE_DISCONNECTED -> bleHandler.obtainMessage(MSG_DISCONNECTED, gatt).sendToTarget()
                }
            } else
                // we report to the upper layer if we lost the connection
                mainHandler.obtainMessage(MSG_CLOSED).sendToTarget()
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mainHandler.obtainMessage(MSG_SERVICES_DISCOVERED, gatt).sendToTarget()
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            //Log.d("DBG", "onDescriptorWrite")
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            //Log.d("DBG", "Characteristic data received")

            mainHandler.obtainMessage(MSG_NOTIFY, characteristic).sendToTarget()
        }
    }

    val HEART_RATE_SERVICE_UUID             = convertFromInteger(0x180D)
    val HEART_RATE_MEASUREMENT_CHAR_UUID    = convertFromInteger(0x2A37)
    val CLIENT_CHARACTERISTIC_CONFIG_UUID   = convertFromInteger(0x2902)

    /* Generates 128-bit UUID from the Protocol Indentifier (16-bit number)
     * and the BASE_UUID (00000000-0000-1000-8000-00805F9B34FB)
     */
    private fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

    data class ServiceCharacteristic(val gatt: BluetoothGatt, val service: UUID, val characteristic: UUID)

    companion object {
        // Ble thread
        private const val MSG_CONNECT = 10
        private const val MSG_CONNECTED = 20
        private const val MSG_DISCONNECT = 30
        private const val MSG_DISCONNECTED = 40
        private const val MSG_GET_NOTIFICATIONS = 50

        // Main thread
        private const val MSG_SERVICES_DISCOVERED = 60
        private const val MSG_NOTIFY = 70
        private const val MSG_CLOSED = 80
    }
}