package ru.ipgames.app.utils

import androidx.recyclerview.widget.DiffUtil
import ru.ipgames.app.model.Server

class ServerDiffUtilCallBackCallback(
    private val oldList: List<Server>,
    private val newList: List<Server>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].address == newList[newItemPosition].address
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].players.now == newList[newItemPosition].players.now &&
                oldList[oldItemPosition].map.name == newList[newItemPosition].map.name
    }
}
