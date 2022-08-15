package com.del.ta_10.ui.order

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.del.ta_10.MainActivity
import com.del.ta_10.data.response.DataDriver
import com.del.ta_10.databinding.ActivityOrderDetaulBinding
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class OrderDetailActivity : AppCompatActivity() {
    private var _binding: ActivityOrderDetaulBinding? = null
    private val binding get() = _binding!!
    private val orderViewModel: OrderViewModel by viewModel()
    private lateinit var sharedPrefLogin: SharedPrefLogin
    var harga: String = "999999"
    var jarak = 0.0
    var waktuTempuh = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderDetaulBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefLogin = SharedPrefLogin(this)

        binding.imgBtnLocation.setOnClickListener {
            val intent = Intent(this, OrderMapActivity::class.java)
            intent.putExtra(OrderMapActivity.PILIH_SENDIRI, false)
            startActivityForResult(intent, 1)
        }


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        supportActionBar?.hide()
        val dataSupir = intent.extras?.getParcelable<DataDriver>(SUPIR)
        val dataIntent = intent.extras
        val dataUser = sharedPrefLogin.getUser()
        if (dataIntent != null) {
            if (dataIntent.getBoolean(OrderMapActivity.PILIH_SENDIRI)){
                binding.apply {
                    edtTitikPenjemputan.setText(dataIntent.getString(CURRENT_LOCATION))
                    edtTitikPengataran.setText(dataIntent.getString(TARGET_LOCATION))
                    txtDistance.text = "Jarak  ${dataIntent.getDouble(DISTANCE)} KM"
                    jarakTempuh.text = "${dataIntent.getInt(DURATION)} Menit"
                    imgBtnLocation.isEnabled = false
                    imgBtnLocationPengantaran.isEnabled = false
                }

                waktuTempuh = dataIntent.getInt(DURATION)

                jarak = dataIntent.getDouble(DISTANCE)

            }
        }

        binding.datePicker.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)

            val picker = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    binding.edtTanggalPengantaran.setText(
                        year.toString() + "/" + (monthOfYear + 1) + "/" + dayOfMonth.toString()
                    )
                },
                year,
                month,
                day
            )
            picker.show()
        }

        binding.apply {


            edtBeratProduk.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if (s.isNotEmpty()) {
                            //v1
//                            var tba = 2000
//                            var tbb = 1500
//                            var tarif = tba + ((jarak - 1) * tbb)
//                            var berat = s.toString().toDouble() / 100
//                            var totalHarga = (tarif + (berat * 5000))
//                            var tenPercenOfHarga = totalHarga * 0.1
//                            harga = (totalHarga + tenPercenOfHarga).toString()
//                            binding.txtTotalHarga.text = "Rp.$harga"

                            //v2


//                            var tm = 5000
//                            var berat = s.toString().toDouble()
//                            var hm = 100
//                            var tarif_jarak = tm * jrk
//                            var tarif_berat = berat * hm
//                            var twoPercentOfHarga = (tarif_jarak + tarif_berat) * 0.02
//                            harga = ((tarif_jarak + tarif_berat) + twoPercentOfHarga).toString()
//                            binding.txtTotalHarga.text = "Rp $harga"

                            //v3

//                            var jrk = jarak
//                            val rB = 12.7
//                            val hB =12.750
//                            val bov = 100000
//                            val bmm = 50*jrk
//
//
//                            var tHarga =(((jrk/rB)*hB)*2) + bov
//
//                            var bp = tHarga * 0.1
//                            tHarga = (tHarga + bp)+ bmm

                            //harga tana berat
//                            harga = tHarga.toString()
//                            binding.txtTotalHarga.text = "Rp.$harga"

                            var jrk = jarak
                            val rB = 12.7
                            val hB =12750
                            val bov = 100000
                            val bmm = 50*jrk

                            var tHarga =(((jrk/rB)*hB)*2)+bov+bmm+(((((jrk/rB)*hB)*2)+bov)*0.1)
                            var hargaPerKg = tHarga / dataSupir?.muatanKendaraan!!.toDouble()

                            harga = (hargaPerKg*s.toString().toDouble()).toInt().toString()
                            binding.txtTotalHarga.text = "Rp.$harga"
                            textDetailRumus.visibility = View.VISIBLE

                            textDetailRumus.setOnClickListener {
                                DialogDetailPerhitungan().detailPerhitungan(this@OrderDetailActivity, jrk, dataSupir.muatanKendaraan, s.toString().toDouble())
                            }
//                            //dengan berat
//                            var hargaPerKg = tHarga / dataSupir?.muatanKendaraan!!.toDouble()
//                            harga = (hargaPerKg *  s.toString().toDouble()).toString()
//                            binding.txtTotalHarga.text = "Rp.${harga}"

                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            var muatanAda = ((dataSupir?.muatanKendaraan?.toInt() ?: 0) - (dataSupir?.muatan_terpenuhi?.toInt()
                ?: 0))
            btnOrder.setOnClickListener {

                var alamat: String = dataUser.alamat.toString()
                var id_kendaraan: String = dataSupir?.idKendaraan.toString()
                var id_petani: String = dataUser.idPetani.toString()
                var status_pembayaran: String = "Proses"
                var bukti_pembayaran: String = "Tidak ada"
                var berat_bawaan: String = binding.edtBeratProduk.text.toString()
                var status: String = "Proses"
                var lokasi_awal: String = binding.edtTitikPenjemputan.text.toString()
                var lokasi_tujuan: String = binding.edtTitikPengataran.text.toString()
                var tgl_penjemputan: String = binding.edtTanggalPengantaran.text.toString()
                var durasi_pengataran = waktuTempuh.toString()

                if (berat_bawaan.toInt() > muatanAda) {
                    Toast.makeText(
                        this@OrderDetailActivity,
                        "Muatan lebih besar dari kapsitas yaitu $muatanAda KG  ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {

                    orderViewModel.orderKendaraan(
                        alamat,
                        id_kendaraan,
                        id_petani,
                        status_pembayaran,
                        bukti_pembayaran,
                        berat_bawaan,
                        harga,
                        status,
                        lokasi_awal,
                        lokasi_tujuan,
                        tgl_penjemputan,
                        durasi_pengataran
                    ).observe(this@OrderDetailActivity, {
                        when (it) {
                            is Resource.Success -> {
                                nestedProgress.visibility = View.GONE
                                Toast.makeText(
                                    binding.root.context,
                                    "Sukses Memesan Orderan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@OrderDetailActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                            is Resource.Loading -> {
                                nestedProgress.visibility = View.VISIBLE
                            }

                            is Resource.Error -> {
                                nestedProgress.visibility = View.GONE
                                Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })


                }

            }
        }

//
//        if (data != null) {
//            binding.edtTitikPenjemputan.setText(data.getString(CURRENT_LOCATION))
//            binding.edtTitikPengataran.setText(data.getString(TARGET_LOCATION))
//            var distanceToMeter = data.getDouble(DISTANCE, 0.0) * 0.001
//            distanceToMeter = Math.round(distanceToMeter * 100.0) / 100.0
//            binding.txtDistance.text = "$distanceToMeter KM"
//        }
    }


//    fun cek(){
//        var jrk = 23.4
//        val rB = 12.7
//        val hB =12750
//        val bov = 100000
//        val bmm = 50*jrk
//
//        var tHarga =(((jrk/rB)*hB)*2)+bov+bmm+(((((jrk/rB)*hB)*2)+bov)*0.1)
//
////      var bp = tHarga * 0.1
////      tHarga = (tHarga + bp)+ bmm
//
////      harga tana berat
//        harga = tHarga.toString()
//        binding.txtTotalHarga.text = "Rp.$tHarga"
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    binding.edtTitikPenjemputan.setText(data.getStringExtra(CURRENT_LOCATION))
                    binding.edtTitikPengataran.setText(data.getStringExtra(TARGET_LOCATION))
                    var distanceToMeter = data.getDoubleExtra(DISTANCE, 0.0) * 0.001
                    var duration = data.getDoubleExtra(DURATION, 0.0)
                    distanceToMeter = Math.round(distanceToMeter * 100.0) / 100.0
                    binding.txtDistance.text = "Jarak  $distanceToMeter KM"
                    binding.jarakTempuh.text = "${Math.round(duration/ 60)} Menit"
                    waktuTempuh = Math.round(duration/ 60).toInt()

                    jarak = distanceToMeter
                }
            }
        }

    }

    companion object {
        const val SUPIR = "SUPIR"
        const val TARGET_LOCATION = "TARGET_LOCATION"
        const val CURRENT_LOCATION = "CURRENT_LOCATION"
        const val DISTANCE = "distance"
        const val DURATION = "duration"
    }
}