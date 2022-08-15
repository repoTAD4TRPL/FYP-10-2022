package com.del.ta_10.util

import androidx.recyclerview.widget.DiffUtil
import com.del.ta_10.data.model.Driver
import com.del.ta_10.data.response.DataDriver

class DriverDiffCallback(private val mOldDriver: List<DataDriver>, private val mNewDriver: List<DataDriver>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldDriver.size
    }

    override fun getNewListSize(): Int {
        return mNewDriver.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDriver[oldItemPosition].jarak == mNewDriver[newItemPosition].jarak
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldDriver = mOldDriver[oldItemPosition]
        val newDriver = mNewDriver[newItemPosition]
        return oldDriver.jarak == newDriver.jarak
    }
}