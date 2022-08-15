package com.del.ta_10.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.R
import com.del.ta_10.data.response.Catatan
import com.del.ta_10.databinding.ListCatatanBinding
import com.del.ta_10.util.CatatanDiffCallback
import com.del.ta_10.util.helper

class CatatanAdapter : RecyclerView.Adapter<CatatanAdapter.CatatanViewHolder>() {
    var catatan = ArrayList<Catatan>()
    fun setData(items: List<Catatan>?) {
        val diffCallback = items?.let { CatatanDiffCallback(this.catatan, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }

        this.catatan.apply {
            clear()
            items?.let { addAll(it) }
        }

        diffResult?.dispatchUpdatesTo(this)
    }

    class CatatanViewHolder(private val binding: ListCatatanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Catatan) {
            binding.apply {
                if (cat.pengaju.equals("supir")) {
                    binding.layoutRes.visibility = View.VISIBLE
                    binding.txtNamaUser1.text = cat.nama
                    binding.txtCatatan1.text = cat.catatan
                    Glide.with(imgUser1)
                        .load(BuildConfig.BASE_URL_FILE + cat.potoProfil)
                        .apply(RequestOptions.centerInsideTransform())
                        .placeholder(R.drawable.dummy_apple)
                        .into(imgUser1)
                    txtTawaran1.text = "Tawaran : ${helper().convertRupiah(cat.harga)}"
                } else if (cat.pengaju.equals("petani")) {
                    binding.layoutUser.visibility = View.VISIBLE
                    binding.txtNamaUser2.text = cat.nama
                    binding.txtCatatan2.text = cat.catatan
                    Glide.with(imgUser2)
                        .load(BuildConfig.BASE_URL_FILE + cat.potoProfil)
                        .apply(RequestOptions.centerInsideTransform())
                        .placeholder(R.drawable.dummy_apple)
                        .into(imgUser2)

                    txtTawaran2.text ="Tawaran : ${helper().convertRupiah(cat.harga)}"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatatanViewHolder =
        CatatanViewHolder(
            ListCatatanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CatatanViewHolder, position: Int) {
        holder.bind(catatan[position])
    }

    override fun getItemCount(): Int = catatan.size
}