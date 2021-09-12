package io.github.kaczmarek.tictactoe.ui.paireddevices

import android.bluetooth.BluetoothClass
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.ui.base.BaseListAdapter
import io.github.kaczmarek.tictactoe.ui.base.BaseRVItem
import io.github.kaczmarek.tictactoe.ui.base.BaseViewHolder
import io.github.kaczmarek.tictactoe.ui.paireddevices.adapter.ItemPairedDevice
import io.github.kaczmarek.tictactoe.ui.paireddevices.adapter.VIEW_TYPE_EMPTY_PAIRED_LIST
import io.github.kaczmarek.tictactoe.ui.paireddevices.adapter.VIEW_TYPE_PAIRED_DEVICE

class PairedDevicesRVAdapter : BaseListAdapter<BaseRVItem, BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            VIEW_TYPE_PAIRED_DEVICE -> SectionViewHolder(view)
            VIEW_TYPE_EMPTY_PAIRED_LIST -> EmptyPlaceholderViewHolder(view)
            else -> throw IllegalStateException("$viewType is unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is SectionViewHolder -> holder.bind(getItem(position) as ItemPairedDevice)
            is EmptyPlaceholderViewHolder -> holder.bind()
        }
    }

    private inner class SectionViewHolder(view: View) : BaseViewHolder(view) {

        private val tvDeviceInfoName = view.findViewById<TextView>(R.id.tvDeviceInfoName)
        private val tvDeviceInfoAddress = view.findViewById<TextView>(R.id.tvDeviceInfoAddress)
        private val clContainer = view.findViewById<ConstraintLayout>(R.id.clDeviceInfoContainer)
        private val ivTypeDevice = view.findViewById<ImageView>(R.id.ivDeviceInfo)

        fun bind(item: ItemPairedDevice) {
            tvDeviceInfoName.text = item.name
            tvDeviceInfoAddress.text = item.address
            ivTypeDevice.setImageResource(
                if (item.type.deviceClass == BluetoothClass.Device.PHONE_SMART) {
                    R.drawable.ic_phone
                } else {
                    R.drawable.ic_other_devices
                }
            )

            clContainer.setOnClickListener {
                onClickListener?.onClick(item)
            }
        }
    }

    private class EmptyPlaceholderViewHolder(view: View) : BaseViewHolder(view) {

        fun bind() {
            // not used
        }
    }
}
