package com.del.ta_10.adapter

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.del.ta_10.R
import com.del.ta_10.data.model.Order
import com.del.ta_10.data.response.DataOrder
import com.del.ta_10.databinding.ListItemOrderBinding
import com.del.ta_10.ui.home.DetailPesananActivity
import com.del.ta_10.util.OrderDiffCallback
import com.del.ta_10.util.helper

class OrderAdapter:RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    val order : ArrayList<DataOrder> = ArrayList()
    var user: String = ""
    fun setData(items: List<DataOrder>?){
        val diffCallback = items?.let { OrderDiffCallback(this.order, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }

        this.order.apply {
            clear()
            items?.let { addAll(it) }
        }

        diffResult?.dispatchUpdatesTo(this)

    }

    fun usr(usr: String?){
        user = usr.toString()
    }

    inner class OrderViewHolder(private val binding: ListItemOrderBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(order: DataOrder){
            binding.apply {
                textOrderBerat.text = " : ${order.beratBawaan} Kg"
                textLokasiAkhir.text = " : ${order.lokasiTujuan}"

                textOrderTanggal.text = order.createdAt
                harga.text = " : ${helper().convertRupiah(order.harga)}"
                var stats = ""

                when(user){
                    "petani"->{
                        textOrderName.text = " : ${order.namaSupir}"
                    }
                    "supir"->{
                        textOrderName.text = " : ${order.namaPetani}"
                    }
                }

                val context = binding.root.context
                when(order.status){
                    "batalkan"->{
                        stats = "Batal"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_batal))
                    }
                    "Selesai"->{
                        stats = "Selesai"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_selesai))
                    }
                    "Di terima supir"->{
                        stats = "Di terima supir"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_proses))
                    }
                    "Di terima petani"->{
                        stats = "Di terima petani"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_proses))
                    }
                    "negosiasi"->{
                        stats = "DiTawar"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_proses))
                    }
                    "Konfrimasi Bukti Selesai"->{
                        stats = "Konfirmasi Bukti"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_proses))
                    }
                    "Proses"->{
                        stats="Proses"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_proses))
                    }

                    "tolak"->{
                        stats = "Ditolak"
                        textOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.color_batal))
                    }
                }
                textOrderStatus.text = stats
            }
            
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(ListItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(order[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,  DetailPesananActivity::class.java)
            intent.putExtra(DetailPesananActivity.PESAAN, order[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = order.size
}