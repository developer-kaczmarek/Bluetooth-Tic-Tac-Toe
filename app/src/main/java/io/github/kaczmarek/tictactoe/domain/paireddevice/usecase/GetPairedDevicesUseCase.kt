package io.github.kaczmarek.tictactoe.domain.paireddevice.usecase

import android.bluetooth.BluetoothDevice
import io.github.kaczmarek.tictactoe.domain.paireddevice.port.PairedDeviceRepository

class GetPairedDevicesUseCase(private val repository: PairedDeviceRepository) {

    suspend fun execute(): List<BluetoothDevice> {
        return repository.getPairedDevices()
    }
}