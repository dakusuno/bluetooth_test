package com.example.bluetooth_test

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.example.bluetooth_test.databinding.ItemWifiBinding
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val bluetoothExtensions = BluetoothExtensions()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startDiscovery()
        val deviceAdapter = DeviceListAdapter()
        val discoverAdapter = DeviceListAdapter()
        val recyclerview:RecyclerView = findViewById(R.id.recyclerview_device_paired)
        recyclerview.apply {
            adapter = deviceAdapter.apply {
                addItems(bluetoothExtensions.bluetoothAdapter.bondedDevices.toList())
            }
        }
        val recyclerConnect = findViewById<RecyclerView>(R.id.recyclerview_device_discovered)
        recyclerConnect.apply {
            bluetoothExtensions.bluetoothDeviceLD.observe(this@MainActivity, Observer {
                Log.d("list",it.toString())
                if(!it.isNullOrEmpty()){
                    this.adapter = discoverAdapter.apply {
                        addItems(it)
                    }
                }
            })
        }
    }

    fun startDiscovery(){
        bluetoothExtensions.bluetoothAdapter.startDiscovery()
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this.registerReceiver(bluetoothExtensions.receiver, filter)
    }
    override fun onDestroy() {
        unregisterReceiver(bluetoothExtensions.receiver)
        super.onDestroy()
    }

}