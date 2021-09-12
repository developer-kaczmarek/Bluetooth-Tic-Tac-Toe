package io.github.kaczmarek.tictactoe.ui.lobby

import androidx.lifecycle.ViewModel
import io.github.kaczmarek.tictactoe.utils.BluetoothService

class LobbyViewModel : ViewModel() {

    fun getClientConnection() = BluetoothService.getClientConnectionObservable()

    fun getConnectionStatus() = BluetoothService.getConnectionStatusObservable()

    fun startServer() {
        BluetoothService.startServer()
    }

    fun startClient() {
        BluetoothService.startClient()
    }
}