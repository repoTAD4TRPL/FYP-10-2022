package com.del.ta_10.util

import androidx.recyclerview.widget.DiffUtil
import com.del.ta_10.data.response.DataLokasi

class LokasiDiffCallback(private val mOldLokasi: List<DataLokasi>, private val mNewLokasi: List<DataLokasi>): DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return mOldLokasi.size
    }

    override fun getNewListSize(): Int {
        return mNewLokasi.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldLokasi[oldItemPosition].createdAt == mNewLokasi[newItemPosition].createdAt
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldOrder = mOldLokasi[oldItemPosition]
        val newOrder = mNewLokasi[newItemPosition]
        return  oldOrder.createdAt == newOrder.createdAt && newOrder.createdAt==newOrder.createdAt
    }

}