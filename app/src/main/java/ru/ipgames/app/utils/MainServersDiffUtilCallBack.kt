package ru.ipgames.app.utils

import android.support.v7.util.DiffUtil
import ru.ipgames.app.model.Servers

class MainServersDiffUtilCallBackCallback(
    private val oldList: List<Servers>,
    private val newList: List<Servers>
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
