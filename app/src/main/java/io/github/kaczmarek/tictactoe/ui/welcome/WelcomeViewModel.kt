package io.github.kaczmarek.tictactoe.ui.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.kaczmarek.tictactoe.utils.BluetoothService

class WelcomeViewModel : ViewModel() {

    private val shouldGoNext = MutableLiveData(false)

    fun getShouldGoNext(): MutableLiveData<Boolean> = shouldGoNext

    fun onNextClicked() {
        shouldGoNext.value =  BluetoothService.getBluetoothIsEnabled()
    }
}