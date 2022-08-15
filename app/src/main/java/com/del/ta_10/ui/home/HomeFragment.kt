package com.del.ta_10.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.R
import com.del.ta_10.adapter.OrderAdapter
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.data.response.DataAuth
import com.del.ta_10.databinding.FragmentHomeBinding
import com.del.ta_10.ui.account.EditProfilActivity
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import com.google.gson.Gson
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequest
import com.del.ta_10.adapter.ProductAdapter
import com.del.ta_10.data.response.OrderResponse
import com.del.ta_10.ui.account.HistoryActivity
import com.del.ta_10.ui.dashboard.DashboardViewModel
import com.del.ta_10.util.MyWorker
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var sharedPref: SharedPrefLogin
    lateinit var dataAuth: DataAuth
    private lateinit var user: DataAuth
    private var productAdapter: ProductAdapter? = null
    private val dashboardViewModel: DashboardViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderAdapter = OrderAdapter()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPref = SharedPrefLogin(binding.root.context)
        binding.layoutFarmer.textNama.text = sharedPref.getUser().namaPetani
        user = sharedPref.getUser()
        productAdapter = ProductAdapter(user.role!!)
        binding.nestedProgress.visibility = View.VISIBLE

        if (user.role == "supir") {
            if (user.jenis_kendaraan == null || user.muatan_kendaraan == null || user.no_tf==null) {
                showDialgFillPorfil()
            }

            binding.layoutFarmer.apply {
                layouProduk.visibility = View.GONE
                nestedProgress.visibility = View.GONE
                txtMuatanTerpenuhi.text = "Muatan ${sharedPref.getUser()?.muatan_terpenuhi}/${sharedPref.getUser()?.muatan_kendaraan}"
                user.stat?.let { setStatus(it) }
            }
        } else {
            if (user.alamat == null || user.alamat=="") {
                showDialgFillPorfil()
            }
            binding.nestedProgress.visibility = View.GONE
            binding.layoutFarmer.layoutStatusDriver.visibility = View.GONE
            binding.layoutFarmer.apply {
                txtMuatanTerpenuhi.visibility = View.GONE
                txtStatus.visibility = View.GONE
                btnUbahStatus.visibility = View.GONE
            }
        }

        binding.layoutFarmer.apply {
            with(rvOrder) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = orderAdapter
            }
            with(rvProduk){
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = productAdapter
            }
        }

        binding.layoutFarmer.apply {
            Glide.with(photoUser)
                .load(BuildConfig.BASE_URL_FILE + sharedPref.getUser().potoProfil)
                .apply(RequestOptions.centerInsideTransform()).placeholder(R.drawable.dummy_apple)
                .into(photoUser)
        }

        binding.layoutFarmer.btnUbahStatus.setOnClickListener {
            ubahStatus()
        }

        binding.layoutFarmer.lihatSelengkapnya.setOnClickListener {
            val intent =  Intent(binding.root.context, HistoryActivity::class.java)
            startActivity(intent)
        }

//        val periodicWork = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
//            .build()
//
//        WorkManager.getInstance().enqueue(periodicWork)


        return root
    }

    private fun showDialgFillPorfil() {
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Profil Anda belum lengkap")
        builder.setMessage("Silahkan Lengkapi Profil Anda")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val intent = Intent(binding.root.context, EditProfilActivity::class.java)
            startActivity(intent)
            Toast.makeText(
                binding.root.context,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(
                binding.root.context,
                android.R.string.no, Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }

    private fun setData() {
        binding.layoutFarmer.apply {
            user.idPetani?.let {
                user.role?.let { it1 ->
                    homeViewModel.getDummyOrder(it, it1).observe(viewLifecycleOwner, {
                        when (it) {
                            is Resource.Success -> {
                                nestedProgress.visibility = View.GONE
                                if(it?.data?.data?.size!! >2){
                                    orderAdapter.setData(it.data?.data?.subList(0,3))
                                }else{
                                    orderAdapter.setData(it.data?.data)
                                }
                                orderAdapter.usr(user.role)
                            }
                            is Resource.Loading -> {
                                nestedProgress.visibility = View.VISIBLE
                            }
                            is Resource.Error -> {
                                Toast.makeText(root.context, it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

//                    RetrofitClient.apiInstance.listOrderByUserRe(it, it1)
//                        .enqueue(object: Callback<OrderResponse>{
//                            override fun onResponse(
//                                call: Call<OrderResponse>,
//                                response: Response<OrderResponse>
//                            ) {
//                                orderAdapter.setData(response?.body()?.data)
//
//                            }
//
//                            override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
//                                TODO("Not yet implemented")
//                            }
//
//                        })
                }
            }
        }
    }

    private fun ubahStatus() {
        binding.nestedProgress.visibility = View.VISIBLE
        var stat = binding.layoutFarmer.txtStatus.text.toString()
        if(stat == "Tersedia"){
            stat = "Dalam Pesanan"
        }else if(stat == "Dalam Pesanan"){
            stat = "Tersedia"
        }
        RetrofitClient.apiInstance.updateStatusKendaraaan(
            sharedPref.getUser().id_kendaraan,
            stat
        )
            .enqueue(object : Callback<CRUDResponse> {
                override fun onResponse(
                    call: Call<CRUDResponse>,
                    response: Response<CRUDResponse>
                ) {
                    Toast.makeText(
                        binding.root.context,
                        response.body()?.massage,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            binding.root.context,
                            response.body()?.massage,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.nestedProgress.visibility = View.GONE
                        setStatus(stat)
                        val tempUser = sharedPref.getUser()
                        tempUser.stat = stat
                        val dt = Gson().toJson(tempUser)
                        sharedPref.resetDataUser(dt)
                    } else Log.d("Error : ", response.errorBody().toString())
                }

                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                    Log.d("Error : ", t.message.toString())
                }

            })

    }

    private fun setProduk(){
        dashboardViewModel.getProductById(sharedPref.getUser().idPetani).observe(this, {
            when(it){
                is Resource.Success->{
                    binding.nestedProgress.visibility = View.GONE
                    productAdapter?.setData(it.data?.data)
                }
                is Resource.Loading->{
                    binding.nestedProgress.visibility = View.VISIBLE
                }
                is Resource.Error->{
                    binding.nestedProgress.visibility = View.GONE
                    Toast.makeText(binding.root.context, "Error ${it.message}", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun setStatus(status:String){
        val context =  binding.root.context
        binding.layoutFarmer.apply {

            when (status) {
                "Dalam Pesanan" -> {
                    txtStatus.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_proses
                        )
                    )
                }

                "Tersedia" -> {
                    txtStatus.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_selesai
                        )
                    )
                }
            }
            txtStatus.text = status
        }
    }

    override fun onResume() {
        super.onResume()
//        orderAdapter.order.clear()
        setData()
        setProduk()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productAdapter = null
    }
}