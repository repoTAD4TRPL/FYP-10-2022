package com.del.ta_10.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.del.ta_10.R
import com.del.ta_10.adapter.LokasiAdapter
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.data.response.DataLokasi
import com.del.ta_10.data.response.LokasiResponse
import com.del.ta_10.util.helper

import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.ext.getOrCreateScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.ViewGroup




class DialogLocation {
    var myLatitude: Double = 0.0
    var myLongitude: Double = 0.0
    private var lokasiAdapter: LokasiAdapter? = null

    fun showDialogDetailTracking(context: Context, id_order: String, role:String) {
        lokasiAdapter = LokasiAdapter()
        BottomSheetDialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.layout_list_tracking_lokasi)
            val addLayout = this.findViewById<LinearLayout>(R.id.layout_add_lokasi)
            addLayout?.visibility = View.GONE
            val edtLokasi = this.findViewById<EditText>(R.id.edt_lokasi)
            val btnLokasiSekarang = this.findViewById<Button>(R.id.btn_lokasi_sekarang)
            val btnTambah = this.findViewById<Button>(R.id.btn_tambah)
            val rvlokasi = this.findViewById<RecyclerView>(R.id.rv_list_tracking)
            if (role == "petani"){
                btnLokasiSekarang?.visibility = View.GONE
            }
            with(rvlokasi){
             this?.layoutManager = LinearLayoutManager(context)
                this?.adapter = lokasiAdapter
            }
            listLokasi(id_order)
            btnLokasiSekarang?.setOnClickListener {
                edtLokasi?.setText(getLastKnownLocation(context))
                addLayout?.visibility = View.VISIBLE
                btnTambah?.setOnClickListener {
                    RetrofitClient.apiInstance.addLokasi(
                        id_order,
                        edtLokasi?.text.toString(),
                        myLatitude.toString(),
                        myLongitude.toString(),
                       "IN DEV"
                    )
                        .enqueue(object : Callback<CRUDResponse> {
                            override fun onResponse(
                                call: Call<CRUDResponse>,
                                response: Response<CRUDResponse>
                            ) {
                                if (response.isSuccessful) {
                                    addLayout?.visibility = View.GONE
                                    Toast.makeText(context, response?.message(), Toast.LENGTH_SHORT)
                                        .show()
                                    listLokasi(id_order)
                                }
                            }

                            override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                                Log.e("Error", t.message.toString())
                            }

                        })
                }
            }


        }.show()
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLastKnownLocation(context: Context): String {

        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null
        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null)
                break
        }
        if (location != null) {
            myLatitude = location.latitude
            myLongitude = location.longitude
            return helper().getAddress(location.latitude, location.longitude, context)
        } else
            return "Data tidak ditemukan"

    }

    fun listLokasi(id: String) {
        RetrofitClient.apiInstance.getListLoksi(id)
            .enqueue(object : Callback<LokasiResponse> {
                override fun onResponse(
                    call: Call<LokasiResponse>,
                    response: Response<LokasiResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data.let {
                            if (it != null) {
                                lokasiAdapter?.setData(it)
                            }
                        }

                    } else {
                        Log.d("errprrr", response?.message())
                    }
                }

                override fun onFailure(call: Call<LokasiResponse>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }

            })

    }
}