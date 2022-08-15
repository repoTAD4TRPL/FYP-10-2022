package com.del.ta_10.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.R
import com.del.ta_10.data.response.DataTani
import com.del.ta_10.databinding.ListItemProductBinding
import com.del.ta_10.ui.dashboard.DashboardFragment
import com.del.ta_10.ui.dashboard.DetailProductActivity
import com.del.ta_10.util.ProductDiffCallback
import java.util.*
import kotlin.collections.ArrayList

class ProductAdapter(private val user: String): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){
    private val product: ArrayList<DataTani> = ArrayList()
    fun setData(items: List<DataTani>?){
        val diffCallback = items?.let { ProductDiffCallback(this.product, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
        this.product.apply {
            clear()
            items?.let { addAll(it) }
        }
        diffResult?.dispatchUpdatesTo(this)
//        notifyDataSetChanged()

    }

    inner class ProductViewHolder(private val binding: ListItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: DataTani, context: Context){
            binding.apply {
                if (user != "pembeli"){

                }else{
                    petaniName.text = product.namaPetani
                }
                productName.text = product.jenisHasilTani
                productPrice.text = "Rp.${product.harga}"
                Glide.with(imgProduct)
                    .load(BuildConfig.BASE_URL_FILE+product.img)
                    .apply(RequestOptions.centerInsideTransform())
                    .placeholder(R.drawable.dummy_apple)
                    .into(imgProduct)
                cardProduct.setOnClickListener {
                    val intent = Intent(context, DetailProductActivity::class.java)
                    intent.putExtra(DashboardFragment.ROLE, user)
                    intent.putExtra(DetailProductActivity.PRODUCT, product)
                    context.startActivity(intent)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(ListItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(product[position], holder.itemView.context)
    }

    override fun getItemCount(): Int{
        return product.size
    }
}