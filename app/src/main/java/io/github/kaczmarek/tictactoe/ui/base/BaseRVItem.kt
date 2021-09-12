package io.github.kaczmarek.tictactoe.ui.base

interface BaseRVItem {
    val itemViewType: Int

    fun getItemId(): Int
}