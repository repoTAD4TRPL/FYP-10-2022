package com.del.ta_10.util

import androidx.recyclerview.widget.DiffUtil
import com.del.ta_10.data.model.Product
import com.del.ta_10.data.response.DataTani

class ProductDiffCallback (private val mOldProduct: List<DataTani>, private val mNewProduct: List<DataTani>): DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return mOldProduct.size
    }

    override fun getNewListSize(): Int {
        return  mNewProduct.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldProduct[oldItemPosition].idHasilPanen == mNewProduct[newItemPosition].idHasilPanen
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = mOldProduct[oldItemPosition]
        val newProduct = mNewProduct[newItemPosition]
        return oldProduct.idHasilPanen == newProduct.idHasilPanen
    }
}