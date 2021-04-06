package com.example.bluetooth_test

import android.bluetooth.BluetoothDevice
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.afollestad.materialdialogs.MaterialDialog
import com.example.bluetooth_test.databinding.ItemWifiBinding
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream

class DeviceViewHolder(view: View) : BaseViewHolder(view) {
    val binding by bindings<ItemWifiBinding>(view)
    private lateinit var listDevice: BluetoothDevice
    private var toastLiveData =""
    private val bluetoothExtensions = BluetoothExtensions()

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
            bluetoothExtensions.connect(listDevice.toString()) {
                toastLiveData = it
            }.also {
                Toast.makeText(context,toastLiveData,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onLongClick(v: View?) = false
}