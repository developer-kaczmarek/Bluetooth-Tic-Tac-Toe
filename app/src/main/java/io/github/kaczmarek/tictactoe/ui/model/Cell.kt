package io.github.kaczmarek.tictactoe.ui.model

class Cell(var player: Player?) {
    fun isEmpty() = player == null
}