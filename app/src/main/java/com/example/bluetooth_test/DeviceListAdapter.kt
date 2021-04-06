package com.example.bluetooth_test

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow

class DeviceListAdapter() : BaseAdapter() {

    init {
        addSection(ArrayList<BluetoothDevice>())
    }

    fun addItems(sampleItems: List<BluetoothDevice>) {
        sections().first().run {
            clear()
            Log.d("addItem", sampleItems.toString())
            addAll(sampleItems)
            notifyDataSetChanged()
        }
    }

    override fun layout(sectionRow: SectionRow): Int {
        return R.layout.item_wifi
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return DeviceViewHolder(view)
    }
}