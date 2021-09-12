package io.github.kaczmarek.tictactoe.ui.game

import android.util.Log
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import io.github.kaczmarek.tictactoe.ui.model.Game
import io.github.kaczmarek.tictactoe.ui.model.PlayerType
import io.github.kaczmarek.tictactoe.utils.BluetoothService
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


class GameViewModel : ViewModel() {
    val playerType = MutableLiveData<PlayerType>()

    val cells = ObservableArrayMap<String, String?>()

    var isNoughtCurrentPlayer = ObservableBoolean()

    private val selectedPlayerType: PlayerType = if (BluetoothService.getIsHost()) {
        PlayerType.HOST
    } else {
        PlayerType.GUEST
    }

    private val game = Game()

    init {
        cells.clear()
        isNoughtCurrentPlayer.set(game.currentPlayer?.value == PlayerType.GUEST)
    }

    fun onClickedCellAt(row: Int, col: Int) {
        if (game.cells[row][col].isEmpty()) {
            if(selectedPlayerType != game.currentPlayer!!.value) {
                Log.i("MY TAG", "Не ваш ход")
            } else {
                game.cells[row][col].player = game.currentPlayer
                cells["$row$col"] = game.currentPlayer?.value?.mark
                if (!game.hasEnded()) game.switchPlayer()
                BluetoothService.send("$row,$col")
                isNoughtCurrentPlayer.set(game.currentPlayer?.value == PlayerType.GUEST)
            }
        }
    }

    fun getMark(row: Int, col: Int) {
        game.cells[row][col].player = game.currentPlayer
        cells["$row$col"] = game.currentPlayer?.value?.mark
        if (!game.hasEnded()) game.switchPlayer()
        isNoughtCurrentPlayer.set(game.currentPlayer?.value == PlayerType.GUEST)
    }

    fun getWinner() = game.winner
}

