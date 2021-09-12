package io.github.kaczmarek.tictactoe.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

object BluetoothService {
    private const val TIMEOUT: Int = 240

    private var serverStarted: Boolean = false
    private var clientStarted: Boolean = false
    private var isHost: Boolean = false

    private val uuid: UUID = UUID.fromString("12165e52-13a8-11ec-82a8-0242ac130003")

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val messageObservable = MutableLiveData<String>()
    private val connectionStatusObservable = MutableLiveData<BluetoothConnectionStatus>()
    private val clientConnectionStatusObservable = MutableLiveData<Boolean>()

    private var serverSocket: BluetoothServerSocket? = null
    private var clientSocket: BluetoothSocket? = null
    private var btDevice: BluetoothDevice? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    private var clientJob: Job? = null
    private var serverJob: Job? = null
    private var listeningJob: Job? = null

    fun getMessageObservable(): MutableLiveData<String> = messageObservable

    fun getConnectionStatusObservable(): MutableLiveData<BluetoothConnectionStatus> =
        connectionStatusObservable

    fun getClientConnectionObservable(): MutableLiveData<Boolean> = clientConnectionStatusObservable

    fun setDevice(device: BluetoothDevice) {
        btDevice = device
    }

    fun getIsHost(): Boolean = isHost

    fun getBluetoothIsEnabled() = bluetoothAdapter?.isEnabled == true

    fun initServer() {
        CoroutineScope(Dispatchers.IO).launch {
            var bluetoothServerSocket: BluetoothServerSocket? = null

            runCatching {
                if (bluetoothAdapter == null) throw NullPointerException("Adapter is Null")
                if (bluetoothAdapter.isEnabled.not()) throw SecurityException("Bluetooth is disabled")

                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    "TicTacToeServer",
                    uuid
                )
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    connectionStatusObservable.value = BluetoothConnectionStatus.CONNECTED
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    connectionStatusObservable.value = BluetoothConnectionStatus.FAILED
                }
            }

            serverSocket = bluetoothServerSocket
        }.start()
    }

    fun startServer() {
        if (serverStarted) return

        serverStarted = true

        serverJob?.cancel()
        serverJob = CoroutineScope(Dispatchers.IO).launch {
            var socket: BluetoothSocket? = null
            var timeout = 0

            while (true) {
                // The host will not wait indefinitely, but instead he wil wait up to two minutes
                if (timeout == TIMEOUT) {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        clientConnectionStatusObservable.value = false
                    }
                    break
                }

                try {
                    if (serverSocket != null) {
                        socket = serverSocket!!.accept()
                    } else {
                        serverStarted = false
                    }
                } catch (e: IOException) {
                    serverStarted = false
                    withContext(Dispatchers.Main) {
                        clientConnectionStatusObservable.value = false
                    }
                    break
                }

                if (socket != null) {
                    try {
                        clientSocket = socket
                        inputStream = socket.inputStream
                        outputStream = socket.outputStream

                        startListening()

                        serverSocket?.close()
                    } catch (e: IOException) {
                        serverStarted = false
                    }
                    withContext(Dispatchers.Main) {
                        clientConnectionStatusObservable.value = true
                    }

                    break
                }

                timeout++
                delay(500)
            }
        }
        isHost = true
        serverJob?.start()
    }

    fun startClient() {
        if (clientStarted) return

        clientStarted = true
        var tmp: BluetoothSocket? = null

        clientJob?.cancel()
        clientJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                tmp = btDevice?.createRfcommSocketToServiceRecord(uuid)
                delay(1000)

                withContext(Dispatchers.Main) {
                    connectionStatusObservable.value = BluetoothConnectionStatus.CONNECTED
                }
            } catch (e: IOException) {
                clientStarted = false
                delay(1000)
                withContext(Dispatchers.Main) {
                    connectionStatusObservable.value = BluetoothConnectionStatus.FAILED
                }
            }

            clientSocket = tmp


            bluetoothAdapter?.cancelDiscovery()

            try {
                if (clientSocket != null) {
                    clientSocket?.let {
                        it.connect()

                        inputStream = it.inputStream
                        outputStream = it.outputStream

                        startListening()
                    }
                } else {
                    clientStarted = false
                }
            } catch (e: IOException) {
                clientStarted = false

                try {
                    clientSocket?.close()
                } catch (exc: IOException) {
                }
            }
        }
        isHost = false
        clientJob?.start()
    }

    fun send(message: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                message?.let {
                    outputStream?.write(it.toByteArray())
                }
            } catch (e: Exception) {
            }
        }.start()
    }

    fun clear() {
        stopServer()
        stopClient()
        serverStarted = false
        clientStarted = false

        serverJob?.cancel()
        clientJob?.cancel()
        listeningJob?.cancel()

        serverSocket = null
        clientSocket = null
        btDevice = null
        inputStream = null
        outputStream = null
        clientJob = null
        serverJob = null
        listeningJob = null
    }

    private fun startListening() {
        listeningJob?.cancel()
        listeningJob = CoroutineScope(Dispatchers.IO).launch {
            inputStream?.let {
                while (true) {
                    if (it.available() == 0) {
                        try {
                            val buffer = ByteArray(1024)
                            val dataInputStream = DataInputStream(it)

                            val bytes = dataInputStream.read(buffer, 0, buffer.size)
                            val message = String(buffer, 0, bytes)

                            delay(300)
                            withContext(Dispatchers.Main) {
                                messageObservable.value = message
                            }
                            //  messageObservable.onNext(message)
                        } catch (e: IOException) {
                        }
                    }

                    delay(300)
                }
            }
        }

        listeningJob?.start()
    }

    private fun stopServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                inputStream?.close()

                outputStream?.let {
                    it.flush()
                    it.close()
                }

                serverSocket?.close()
            } catch (e: IOException) {
            }
        }.start()
    }

    private fun stopClient() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                inputStream?.close()

                outputStream?.let {
                    it.flush()
                    it.close()
                }

                clientSocket?.close()
            } catch (e: Exception) {

            }
        }.start()
    }
}

enum class BluetoothConnectionStatus {
    CONNECTED,
    FAILED
}