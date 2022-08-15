package com.del.ta_10.util

import androidx.recyclerview.widget.DiffUtil
import com.del.ta_10.data.model.Order
import com.del.ta_10.data.response.DataOrder

class OrderDiffCallback(private val mOldOrder: List<DataOrder>, private val mNewOrder: List<DataOrder>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldOrder.size
    }

    override fun getNewListSize(): Int {
        return mNewOrder.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldOrder[oldItemPosition].idOrder == mNewOrder[newItemPosition].idOrder
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldOrder = mOldOrder[oldItemPosition]
        val newOrder = mNewOrder[newItemPosition]
        return  oldOrder.idOrder == newOrder.idOrder
    }
}