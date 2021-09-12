package io.github.kaczmarek.tictactoe.ui.model

import android.util.Log
import androidx.lifecycle.MutableLiveData

class Game {
    val playerX: Player = Player(PlayerType.HOST)
    val playerO: Player = Player(PlayerType.GUEST)
    var currentPlayer: Player? = this.playerX
    val cells = Array(3) { Array(3) { Cell(null) } }
    val winner = MutableLiveData<Player?>()

    fun switchPlayer() {
        Log.i("MY TAG", "currentPlayer = ${if (currentPlayer == playerX) playerO else playerX}")
        currentPlayer = if (currentPlayer == playerX) playerO else playerX
    }

    fun isBoardFull() = cells.flatten().none { it.isEmpty() }

    fun hasEnded(): Boolean {
        Log.i("MY TAG", "hasEnded")
        return when {
            hasHorizontalSolution() || hasVerticalSolution() || hasDiagonalSolution() -> {
                winner.value = currentPlayer
                Log.i("MY TAG", "hasEnded true")
                true
            }
            isBoardFull() -> {
                winner.value = null
                Log.i("MY TAG", "hasEnded true")
                true
            }
            else -> {
                Log.i("MY TAG", "hasEnded false")
                false
            }
        }
    }

    private fun hasHorizontalSolution() =
        cells.any { it.all { it.player == playerX } || it.all { it.player == playerO } }

    private fun hasVerticalSolution(): Boolean {
        (0..2).forEach {
            val firstCell = cells[0][it]
            if (!firstCell.isEmpty()
                && firstCell.player == cells[1][it].player
                && firstCell.player == cells[2][it].player
            )
                return true
        }
        return false
    }

    private fun hasDiagonalSolution(): Boolean {
        val centerCell = cells[1][1]
        return !centerCell.isEmpty() &&
                ((centerCell.player == cells[0][0].player && centerCell.player == cells[2][2].player) ||
                        (centerCell.player == cells[0][2].player && centerCell.player == cells[2][0].player))
    }
}