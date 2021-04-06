package com.example.bluetooth_test

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.util.*


class BluetoothExtensions {
   val bluetoothAdapter:BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val visibleDevice : List<BluetoothDevice> = bluetoothAdapter.bondedDevices.toList()
    val bluetoothDeviceLD= MutableLiveData<List<BluetoothDevice>>()

    val arrayDevice = ArrayList<BluetoothDevice>()
    suspend fun connect(device:String,message:(String)->Unit) {
        return withContext(Dispatchers.IO) {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
                try {
                    val bluetoothAddress = device // replace with your device's address
                    val bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothAddress)
                    var bluetoothSocket = bluetoothDevice?.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    )
                    bluetoothSocket?.connect()
                    if (bluetoothSocket != null) {
                        if(bluetoothSocket.isConnected){
                            message("Success")
                        }
                    }
                    bluetoothSocket?.inputStream?.close()
                    bluetoothSocket?.outputStream?.close()
                    bluetoothSocket?.close()
                } catch (e: Exception){
                    message(e.message.toString())
                }
            }
        }
    }
    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND == action) {
                //bluetooth device found
                val device =  intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                device?.let { arrayDevice.add(it) }
                bluetoothDeviceLD.postValue(arrayDevice)
            }
        }
    }


}