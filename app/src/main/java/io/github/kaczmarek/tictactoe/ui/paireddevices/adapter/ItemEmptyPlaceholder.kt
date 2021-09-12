package io.github.kaczmarek.tictactoe.ui.paireddevices.adapter

import io.github.kaczmarek.tictactoe.R
import io.github.kaczmarek.tictactoe.ui.base.BaseRVItem

const val VIEW_TYPE_EMPTY_PAIRED_LIST = R.layout.rv_item_empty_paired_list

data class ItemEmptyPlaceholder(
    override val itemViewType: Int = VIEW_TYPE_EMPTY_PAIRED_LIST
) : BaseRVItem {

    override fun getItemId() = itemViewType.hashCode()
}