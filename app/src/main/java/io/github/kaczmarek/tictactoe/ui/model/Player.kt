package io.github.kaczmarek.tictactoe.ui.model

class Player(val value: PlayerType)

enum class PlayerType(val mark:String) {
    HOST("X"), GUEST("0")
}
