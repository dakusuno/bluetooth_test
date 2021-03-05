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
    private var outputStream: OutputStream? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_connect.setOnClickListener {
            bluetoothExtensions.connectBottomSheet(BottomSheet(LayoutMode.WRAP_CONTENT),this,this)
        }
        

        button_print.setOnClickListener {
            outputStream?.run {
                write("hello World!".toByteArray())
                write(byteArrayOf(10))          // Feed line
            }
        }

    }


    override fun onDestroy() {
        unregisterReceiver(bluetoothExtensions.receiver)
        super.onDestroy()
    }

}