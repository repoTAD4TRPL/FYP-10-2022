package com.del.ta_10.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.del.ta_10.data.response.DataLokasi
import com.del.ta_10.databinding.ListItemLocationBinding
import com.del.ta_10.databinding.ListItemOrderBinding
import com.del.ta_10.util.DriverDiffCallback
import com.del.ta_10.util.LokasiDiffCallback

class LokasiAdapter: RecyclerView.Adapter<LokasiAdapter.LokasiViewHolder>() {

    val lokasi: ArrayList<DataLokasi> = ArrayList()
    fun setData(items: List<DataLokasi>?){
        val diffCallback = items?.let { LokasiDiffCallback(this.lokasi, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }

        this.lokasi.apply {
            clear()
            items?.let { addAll(it) }
        }

        diffResult?.dispatchUpdatesTo(this)
    }

    inner class LokasiViewHolder(private val binding: ListItemLocationBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(lok: DataLokasi){
            binding.textLokasi.text = lok.lokasiSaatIni
            binding.tgl.text = lok.createdAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LokasiViewHolder =
        LokasiViewHolder(ListItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: LokasiViewHolder, position: Int) {
        holder.bind(lokasi[position])
    }

    override fun getItemCount(): Int = lokasi.size
}