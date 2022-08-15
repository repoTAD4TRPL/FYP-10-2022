package com.del.ta_10.ui.home

import android.content.Context
import android.util.Log
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.del.ta_10.R
import com.del.ta_10.adapter.CatatanAdapter
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.Catatan
import com.del.ta_10.data.response.CatatanResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogCatatan {
    var dataCatatan = ArrayList<Catatan>()
    fun getListCatatan(context: Context, id: String) {
        var catatanAdapter = CatatanAdapter()
        BottomSheetDialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.layouat_list_catatan)
            var rvCatatan = this.findViewById<RecyclerView>(R.id.rv_list_catatan)
            with(rvCatatan) {
                this?.layoutManager = LinearLayoutManager(context)
                this?.adapter = catatanAdapter
            }

            RetrofitClient.apiInstance.getCatatan(id)
                .enqueue(object : Callback<CatatanResponse> {
                    override fun onResponse(
                        call: Call<CatatanResponse>,
                        response: Response<CatatanResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.data.let { it1 ->
                                if (it1 != null) {
                                    dataCatatan.addAll(it1)
                                }
                            }
                        }

                        var sort = dataCatatan.sortedWith(compareBy({it.id}))
                        catatanAdapter.setData(sort)
                    }

                    override fun onFailure(call: Call<CatatanResponse>, t: Throwable) {
                        Log.d("error", t.message.toString())
                    }

                })
        }.show()
    }
}