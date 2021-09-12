package io.github.kaczmarek.tictactoe.ui.paireddevices.adapter

import android.bluetooth.BluetoothClass
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.ui.base.BaseRVItem

const val VIEW_TYPE_PAIRED_DEVICE = R.layout.rv_item_device_info

data class ItemPairedDevice(
    val name: String,
    val address: String,
    val type: BluetoothClass,
    override val itemViewType: Int = VIEW_TYPE_PAIRED_DEVICE
) : BaseRVItem {

    override fun getItemId() = name.hashCode() + address.hashCode()
}