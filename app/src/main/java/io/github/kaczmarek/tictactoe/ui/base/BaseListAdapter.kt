package io.github.kaczmarek.tictactoe.ui.base

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<D : BaseRVItem, VH : BaseViewHolder> constructor(
    callback: DiffUtil.ItemCallback<D> = DiffUtilCallback()
) : ListAdapter<D, VH>(callback) {
    var onClickListener: OnClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemViewType
    }

    fun update(list: List<D>?) {
        this.submitList(list?.let { ArrayList(list) } ?: emptyList())
    }

    interface OnClickListener {

        fun onClick(item: BaseRVItem)
    }
}

class DiffUtilCallback<D : BaseRVItem> : DiffUtil.ItemCallback<D>() {
    override fun areItemsTheSame(oldItem: D, newItem: D): Boolean {
        return oldItem.getItemId() == newItem.getItemId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: D, newItem: D): Boolean {
        return oldItem == newItem
    }
}