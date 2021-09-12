package io.github.kaczmarek.tictactoe.ui.chooser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.kaczmarek.tictactoe.utils.BluetoothService
import io.github.kaczmarek.tictactoe.utils.BluetoothConnectionStatus

class ChooserViewModel : ViewModel() {

    fun getBluetoothConnectionStatus(): MutableLiveData<BluetoothConnectionStatus> =
        BluetoothService.getConnectionStatusObservable()


    fun initServer() {
        BluetoothService.initServer()
    }
}