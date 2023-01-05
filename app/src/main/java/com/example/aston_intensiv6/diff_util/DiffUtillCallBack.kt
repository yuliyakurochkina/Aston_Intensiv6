package com.example.aston_intensiv6.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.example.aston_intensiv6.data.Contact

class DiffUtilCallBack(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return ((oldItem.id) == (newItem.id))
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return (oldItem.number) == newItem.number
                && (oldItem.name) == newItem.name
                && (oldItem.surname) == newItem.surname
                && (oldItem.picId) == newItem.picId
    }
}