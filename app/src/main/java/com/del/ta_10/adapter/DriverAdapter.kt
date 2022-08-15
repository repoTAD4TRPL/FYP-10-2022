package com.del.ta_10.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.R
import com.del.ta_10.data.model.Driver
import com.del.ta_10.data.response.DataDriver
import com.del.ta_10.databinding.ListItemDriverBinding
import com.del.ta_10.ui.order.OrderDetailActivity
import com.del.ta_10.ui.order.OrderMapActivity
import com.del.ta_10.util.DriverDiffCallback

class DriverAdapter(private val onClick: (DataDriver) -> Unit): RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {
    val driver: ArrayList<DataDriver> = ArrayList()
    fun setData(items: List<DataDriver>?){
        val diffCallback = items?.let { DriverDiffCallback(this.driver, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
        this.driver.apply {
            clear()
            items?.let { addAll(it) }
        }

        diffResult?.dispatchUpdatesTo(this)
    }
    inner class DriverViewHolder(private val binding: ListItemDriverBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(driver: DataDriver, context: Context){
            binding.apply {
//                var muatanAda = ((driver.muatanKendaraan?.toInt() ?: 0) - (driver.muatan_terpenuhi?.toInt()
//                    ?: 0))
                driverName.text = "Nama Supir: "+driver.nama
                driverAddress.text = "Alamat : "+ driver.alamat
                typeDriver.text = "Jenis Kendaraan : "+ driver.jenisKendaraan
                noTelp.text = "No. HP : "+ driver.noTelepon
                status.text = "Status: "+ driver.stat
                muatanTersedia.text = "Muatan Tersedia: ${driver.muatan_terpenuhi} / ${driver.muatanKendaraan}"
                jarak.text = "Jarak: ${String.format("%.2f", driver.jarak)} KM"


//                    orderCard.setOnClickListener {
//                        if(driver.stat!="Tersedia"){
//                            Toast.makeText(binding.root.context, "Pengendara ini sedang tidak menerima pesanan", Toast.LENGTH_SHORT).show()
//                        }else{
//                        val intent = Intent(context, OrderDetailActivity::class.java)
//                        intent.putExtra(OrderDetailActivity.SUPIR, driver)
//                        context.startActivity(intent)
//                    }
//                }

                Glide.with(imgPengemudi)
                    .load(BuildConfig.BASE_URL_FILE+driver.potoProfil)
                    .placeholder(R.drawable.driver_icon)
                    .into(imgPengemudi)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder =
        DriverViewHolder(ListItemDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bind(driver[position], holder.itemView.context)
        holder.itemView.setOnClickListener {
            onClick(driver[position])
        }
    }

    override fun getItemCount(): Int = driver.size
}