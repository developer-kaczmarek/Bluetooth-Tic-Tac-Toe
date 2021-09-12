package io.github.kaczmarek.tictactoe.domain.paireddevice.port

import android.bluetooth.BluetoothDevice

interface PairedDeviceRepository {

    suspend fun getPairedDevices(): List<BluetoothDevice>
}