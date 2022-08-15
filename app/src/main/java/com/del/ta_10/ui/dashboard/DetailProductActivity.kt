package com.del.ta_10.ui.dashboard

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.del.ta_10.R
import com.del.ta_10.data.response.DataTani
import com.del.ta_10.databinding.ActivityDetailProductBinding
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.AuthResponse
import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.ui.auth.LoginActivity
import com.del.ta_10.ui.order.OrderDetailActivity
import com.del.ta_10.util.SharedPrefLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailProductActivity: AppCompatActivity() {
    private var _binding: ActivityDetailProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefLogin: SharedPrefLogin
    private var dataIntent : DataTani? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefLogin = SharedPrefLogin(this)
        val role = intent.extras?.getString(DashboardFragment.ROLE)

        dataIntent = intent.extras?.getParcelable(PRODUCT)
        var url = "https://api.whatsapp.com/send?phone=${dataIntent?.noTelepon}"
        binding.apply {
            setData()

            binding.btnBack.setOnClickListener {
                onBackPressed()
            }

            if (sharedPrefLogin.getStatusLogin() && sharedPrefLogin.getUser().role=="petani"){
                if(role=="pembeli"){
                    btnDetailBuy.visibility = View.VISIBLE
                    btnEdit.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                    btnDetailBuy.setText("Hubungi Penjual")
                }else if (sharedPrefLogin.getUser().role!=null){
                    btnDetailBuy.visibility = View.GONE
                    btnEdit.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE

                    btnDelete.setOnClickListener {
                        RetrofitClient.apiInstance.deleteHasilTani(dataIntent?.idHasilPanen.toString())
                            .enqueue(object : Callback<CRUDResponse> {
                                override fun onResponse(
                                    call: Call<CRUDResponse>,
                                    response: Response<CRUDResponse>
                                ) {
                                    if (response.isSuccessful){
                                        Toast.makeText(this@DetailProductActivity, response.body()?.massage, Toast.LENGTH_SHORT).show()
                                        if(response.body()?.status=="ok"){
                                            finish()
                                        }
                                    }else{
                                        println("eRRORR")
                                    }
                                }

                                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                                    Toast.makeText(this@DetailProductActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                                }

                            })
                    }

                    btnEdit.setOnClickListener {
                        val intent = Intent(this@DetailProductActivity, AddProductActivity::class.java)
                        intent.putExtra(PRODUCT, dataIntent)
                        startActivityForResult(intent, 1)
                    }
                }
            }else{
                btnDetailBuy.visibility = View.VISIBLE
                btnEdit.visibility = View.GONE
                btnDelete.visibility = View.GONE
                btnDetailBuy.setText("Hubungi Penjual")
            }

            btnDetailBuy.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }

    fun setData(){
        binding.apply {
            Glide.with(coverDetail)
                .load(BuildConfig.BASE_URL_FILE+dataIntent?.img)
                .apply(RequestOptions.centerInsideTransform()).placeholder(R.drawable.dummy_apple)
                .into(coverDetail)
            productName.text = dataIntent?.jenisHasilTani
            productPrice.text = "Rp. ${dataIntent?.harga}/KG"
            productTanggal.text = dataIntent?.createdAt
            textUsername.text = "Username : "+dataIntent?.username
            namaPetani.text = "Di post oleh : "+dataIntent?.namaPetani
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    dataIntent = data.getParcelableExtra(PRODUCT)
                    setData()
                }
            }
        }

    }


    companion object{
        const val PRODUCT = "product"
    }
}