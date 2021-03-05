package com.example.bluetooth_test

import android.bluetooth.BluetoothDevice
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.bluetooth_test.databinding.ItemWifiBinding
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream

class DeviceViewHolder(view: View, val materialDialog: MaterialDialog) : BaseViewHolder(view) {
    val binding by bindings<ItemWifiBinding>(view)
    private lateinit var listDevice: BluetoothDevice
    private val bluetoothExtensions = BluetoothExtensions()
    private var outputStream: OutputStream? = null

    override fun bindData(data: Any) {
        if (data is BluetoothDevice) {
            listDevice = data
            drawItem()
        }
    }

    private fun drawItem() {
        binding.apply {
            this.name = listDevice.name
            executePendingBindings()
        }
    }

    override fun onClick(v: View?) {
        GlobalScope.launch (Dispatchers.Main) {
            if(outputStream == null) {
                outputStream = bluetoothExtensions.connect(listDevice.toString()).also {
                    Toast.makeText(context, "Printer Connected", Toast.LENGTH_SHORT).show()
                    materialDialog.dismiss()
                }
            }
        }
    }

    override fun onLongClick(v: View?) = false
}