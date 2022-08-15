package com.del.ta_10.util

import androidx.recyclerview.widget.DiffUtil
import com.del.ta_10.data.response.Catatan

class CatatanDiffCallback(private val mOldCatatan: List<Catatan>, private val mNewCatatan: List<Catatan>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldCatatan.size
    }

    override fun getNewListSize(): Int {
        return mNewCatatan.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldCatatan[oldItemPosition].id == mNewCatatan[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldOrder = mOldCatatan[oldItemPosition]
        val newOrder = mNewCatatan[newItemPosition]
        return  oldOrder.id == newOrder.id
    }
}