package io.github.kaczmarek.tictactoe.ui.paireddevices

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.kaczmarek.tictactoe.domain.paireddevice.usecase.GetPairedDevicesUseCase
import io.github.kaczmarek.tictactoe.ui.base.BaseRVItem
import io.github.kaczmarek.tictactoe.ui.paireddevices.adapter.ItemEmptyPlaceholder
import io.github.kaczmarek.tictactoe.ui.paireddevices.adapter.ItemPairedDevice
import io.github.kaczmarek.tictactoe.utils.BluetoothService
import kotlinx.coroutines.*

class PairedDevicesViewModel(
    private val getPairedDevicesUseCase: GetPairedDevicesUseCase
) : ViewModel() {
    private val errorMessage = MutableLiveData<String?>()
    private val pairedDevices = MutableLiveData<List<BaseRVItem>>()
    private val bluetoothDevices = MutableLiveData<List<BluetoothDevice>>()
    private val shouldGoToLobby = MutableLiveData(false)
    private var job: Job? = null

    init {
        job = CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    getPairedDevicesUseCase.execute()
                }
            }.onSuccess {
                bluetoothDevices.value = it
                val devices = arrayListOf<BaseRVItem>()
                if (it.isEmpty()) {
                    devices.add(ItemEmptyPlaceholder())
                } else {
                    it.forEach { device ->
                        devices.add(
                            ItemPairedDevice(
                                device.name,
                                device.address,
                                device.bluetoothClass
                            )
                        )
                    }
                }
                pairedDevices.postValue(devices)
            }.onFailure {
                errorMessage.value = it.message
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun getShouldGoToLobby() = shouldGoToLobby

    fun getPairedDevices() = pairedDevices

    fun getErrorMessage() = errorMessage

    fun onDeviceItemClicked(item: ItemPairedDevice) {
        val device = bluetoothDevices.value?.find {
            it.name == item.name && it.address == item.address && it.bluetoothClass == item.type
        }
        shouldGoToLobby.value = device?.let {
            BluetoothService.setDevice(device)
            true
        } ?: false
    }
}