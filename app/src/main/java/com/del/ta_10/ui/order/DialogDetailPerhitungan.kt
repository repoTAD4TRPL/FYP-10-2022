package com.del.ta_10.ui.order

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.del.ta_10.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text
import android.view.ViewGroup
import android.widget.ImageButton


class DialogDetailPerhitungan {
    var rumus = "Cost =(((Dis/Rb) x HB)*2) + Bov + Bmm + Bp  \n" +
            "\n" +
            "Keterangan:\n" +
            "Dis\t: Jarak (Km)\n" +
            "RB\t: Rasio BBM\t\t\t= 12,7 Km/liter\n" +
            "HB\t: Harga BBM\t\t\t= Rp 12.750,-\n" +
            "Bov\t: Biaya Overhead\t\t\t= Rp 100.000\n" +
            "Bmmt\t: Biaya Maintenance & Ban\t= Rp 50/km\n" +
            "Bp\t: Biaya Profit\t\t\t= 10%\n"

    fun detailPerhitungan(context:Context ,jrk: Double, muatanKendaraan:String, muatanUser:Double){
        val rB = 12.7
        val hB =12750
        val bov = 100000
        val bmm = 50*jrk
        var tHarga =(((jrk/rB)*hB)*2)+bov+bmm+(((((jrk/rB)*hB)*2)+bov)*0.1)
        var hargaPerKg = tHarga / muatanKendaraan.toDouble()
        var  harga = (hargaPerKg*muatanUser).toInt().toString()

        Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.layout_detail_perhitungan)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            window?.setLayout(width, height)
            val txtRumus = this.findViewById<TextView>(R.id.txt_rumus)
            var jarak =  this.findViewById<TextView>(R.id.txt_jarak)
            var rasioBBM =  this.findViewById<TextView>(R.id.txt_rb)
            var hargaBBM =  this.findViewById<TextView>(R.id.txt_hb)
            var bov_text =  this.findViewById<TextView>(R.id.txt_bov)
            var bmm_text =  this.findViewById<TextView>(R.id.txt_bmm)
            var hargaperkg_text =  this.findViewById<TextView>(R.id.txt_harga_per_kg)
            val hasilHargaPerKg = this.findViewById<TextView>(R.id.txt_hasil_harga_per_kg)
            var totalHarga_text =  this.findViewById<TextView>(R.id.txt_total_harga)
            var totalPembayaran = this.findViewById<TextView>(R.id.txt_total_pembayaran)
            val hasilTotalPembayaran = this.findViewById<TextView>(R.id.txt_hasil_total_pembayaran)
            val totalHasilHarga = this.findViewById<TextView>(R.id.txt_hasil_total_harga)
            val imgClose = this.findViewById<ImageButton>(R.id.imageButtonClose)
            jarak?.text = jrk.toString()
            rasioBBM?.text = rB.toString()
            hargaBBM?.text = hB.toString()
            bov_text?.text = bov.toString()
            bmm_text?.text = bmm.toString()
            totalHarga_text?.text = "(((Dis/Rb) x HB)*2) + Bov + Bmm + Bp\n" +
                    "= ((($jrk/$rB) x $hB)x2) + $bov + $bmm + x 0.1"
            totalHasilHarga?.text = "= $tHarga"

            hargaperkg_text?.text = "ΣCost / Muatan Kendaraan"
            hasilHargaPerKg?.text =  "= $hargaPerKg"

            totalPembayaran?.text = "ΣCost/Kg x Muatan User"
            hasilTotalPembayaran?.text = "= $harga"
            txtRumus?.text = rumus
            imgClose.setOnClickListener {
                dismiss()
            }


        }.show()
    }
}