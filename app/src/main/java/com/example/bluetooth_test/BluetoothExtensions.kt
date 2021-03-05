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
    fun isEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }
    suspend fun connect(device:String): OutputStream? {
        return withContext(Dispatchers.IO) {
            var outputStream: OutputStream? = null
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
                try {
                    val bluetoothAddress = device // replace with your device's address
                    val bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothAddress)
                    val bluetoothSocket = bluetoothDevice?.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    )
                    bluetoothAdapter.cancelDiscovery()
                    bluetoothSocket?.connect()
                    if (bluetoothSocket!!.isConnected) {
                        outputStream = bluetoothSocket.outputStream
                        outputStream.write("Device Connected".toByteArray())
                        outputStream.write("Device Connected".toByteArray())
                        outputStream.write("Device Connected".toByteArray())
                        outputStream.write("Device Connected".toByteArray())
                        outputStream.write("Device Connected".toByteArray())
                    }
                } catch (e: Exception){
                    Log.d("TAG", "connect: ${e.message}")
                }
            }
            outputStream
        }
    }
    fun connectBottomSheet(dialog: DialogBehavior = ModalDialog, lifecycleOwner: LifecycleOwner,context: Context) {
        val filter = IntentFilter()
        bluetoothAdapter.startDiscovery()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(receiver, filter)

        MaterialDialog(context, dialog).show {
            val deviceAdapter = DeviceListAdapter(this)
            val discoverAdapter = DeviceListAdapter(this)
            setTitle(R.string.bluetooth_list)

            customView(R.layout.bottomsheet_connect, scrollable = true, horizontalPadding = true)
            val recyclerview: RecyclerView = findViewById(R.id.recyclerview_device_paired)
            recyclerview.apply {
                adapter = deviceAdapter.apply {
                    addItems(bluetoothAdapter.bondedDevices.toList())
                }
            }
            val recyclerConnect = findViewById<RecyclerView>(R.id.recyclerview_device_discovered)
            recyclerConnect.apply {
                bluetoothDeviceLD.observe(lifecycleOwner, Observer {
                    Log.d("list",it.toString())
                    if(!it.isNullOrEmpty()){
                        this.adapter = discoverAdapter.apply {
                            addItems(it)
                        }
                    }
                })
            }

            negativeButton(android.R.string.cancel)
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