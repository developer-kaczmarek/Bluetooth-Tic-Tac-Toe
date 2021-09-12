package io.github.kaczmarek.tictactoe.data.paireddevice.port

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import io.github.kaczmarek.tictactoe.domain.paireddevice.entity.PairedDevice
import io.github.kaczmarek.tictactoe.domain.paireddevice.port.PairedDeviceRepository

class PairedDeviceRepositoryImpl : PairedDeviceRepository {

    override suspend fun getPairedDevices(): List<BluetoothDevice> {
        val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        return adapter?.bondedDevices?.toList() ?: emptyList()
    }
}