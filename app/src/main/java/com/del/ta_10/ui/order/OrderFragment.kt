package com.del.ta_10.ui.order

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.del.ta_10.R
import com.del.ta_10.adapter.DriverAdapter
import com.del.ta_10.adapter.OrderAdapter
import com.del.ta_10.data.response.DataDriver
import com.del.ta_10.databinding.OrderFragmentBinding
import com.del.ta_10.domain.model.Order
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.coroutines.*
import okhttp3.internal.wait
import org.koin.android.scope.scope
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.ext.scope
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class OrderFragment : Fragment(), CoroutineScope  {

    private val orderViewModel: OrderViewModel by viewModel()
    private var driverAdapter: DriverAdapter? = null
    private var _binding: OrderFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var root: View
    var listDriver = ArrayList<DataDriver>()
    var listDriverFilter = ArrayList<DataDriver>()
    private var currentRoute: DirectionsRoute? = null
    private lateinit var sharedPrefLogin: SharedPrefLogin
    var distance: Double? = 0.0
    var jenis = "Pilih Kategori"
    var kategoriStatus = "Pilih Status"
    private lateinit var origin: Point
    var lat = 0.0
    var lon = 0.0
    var jarak = 0.0
    var waktuTempuh = 0
    var currenLocation = ""
    var targetLocation = ""
    var cek = 0

    override val coroutineContext: CoroutineContext
        get() = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrderFragmentBinding.inflate(inflater, container, false)
        root = binding.root

        driverAdapter = DriverAdapter(onClick = { driver ->
            kotlin.run {
                if (driver.stat != "Tersedia") {
                    Toast.makeText(
                        binding.root.context,
                        "Pengendara ini sedang tidak menerima pesanan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.putExtra(OrderDetailActivity.SUPIR, driver)
                    if (lat!=0.0){
                        intent.putExtra(OrderMapActivity.PILIH_SENDIRI, true)
                        intent.putExtra(OrderMapActivity.lat, lat)
                        intent.putExtra(OrderMapActivity.lon, lon)
                        intent.putExtra(OrderDetailActivity.DISTANCE, jarak)
                        intent.putExtra(OrderDetailActivity.DURATION, waktuTempuh)
                        intent.putExtra(OrderDetailActivity.CURRENT_LOCATION, currenLocation)
                        intent.putExtra(OrderDetailActivity.TARGET_LOCATION, targetLocation)
                    }else{
                        intent.putExtra(OrderMapActivity.PILIH_SENDIRI, false)
                    }
                    startActivity(intent)
                }
            }
        })
        sharedPrefLogin = SharedPrefLogin(binding.root.context)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialog()

        val spineer = binding.filterDriver
        val listFilter = resources.getStringArray(R.array.filter_driver)
        if (spineer != null) {
            val adapter =
                ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, listFilter)
            spineer.adapter = adapter
        }

        val spineerStatus = binding.filterDriverTersedia
        val listFilterStatus = resources.getStringArray(R.array.filter_order)
        if (spineerStatus != null) {
            val adapter =
                ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_spinner_item,
                    listFilterStatus
                )
            spineerStatus.adapter = adapter
        }

        spineer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                jenis = listFilter[position]
                if(cek>0){
                    fillter(kategoriStatus, jenis)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }

        spineerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                kategoriStatus = listFilterStatus[position]
                if(cek>0){
                    fillter(kategoriStatus, jenis)
                }
                cek++
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }

        binding.rvDriver.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = driverAdapter
        }

        binding.btnSearch.setOnClickListener {
            searchDriver()
        }

        binding.etSearch.setOnKeyListener { _, KeyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && KeyCode == KeyEvent.KEYCODE_ENTER) {
                searchDriver()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

    }

    private fun showDialog() {
        if (lat == 0.0) {
            Dialog(binding.root.context).apply {
                requestWindowFeature(android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                setCancelable(true)
                setContentView(R.layout.layout_order)
                val viewWithDevice = findViewById<CardView>(R.id.card_view_by_devoce)
                val viewWithoutDevice = findViewById<CardView>(R.id.card_view_pilih_sendiri)
                viewWithDevice.setOnClickListener {
                    showData()
                    this.dismiss()
                }

                viewWithoutDevice.setOnClickListener {
                    val intent = Intent(binding.root.context, OrderMapActivity::class.java)
                    intent.putExtra(OrderMapActivity.PILIH_SENDIRI, true)
                    startActivityForResult(intent, 1)
                    this.dismiss()
                }
            }.show()
        }
    }

    private fun fillter(status: String, katerogri: String) {

        listDriverFilter.clear()

        if (katerogri == "Pilih Kategori" && status == "Pilih Status") {
            listDriverFilter.addAll(listDriver)
        } else if (status == "Pilih Status" && katerogri != "Pilih Kategori") {
            for (cek in listDriver) {
                if (cek.jenisKendaraan!!.contains(katerogri)) {
                    listDriverFilter.add(cek)
                }
            }
        } else if ((status != "Pilih Status") && (katerogri == "Pilih Kategori")) {
            for (cek in listDriver) {
                if (cek.stat!!.contains(status)) {
                    listDriverFilter.add(cek)
                }
            }
        } else {
            for (cek in listDriver) {
                if (cek.stat!!.contains(status) && cek.jenisKendaraan!!.contains(katerogri)) {
                    listDriverFilter.add(cek)
                }
            }
        }

        if(listDriverFilter.size<=0){
            binding.layoutNotFound.visibility = View.VISIBLE
        }else{
            binding.layoutNotFound.visibility = View.GONE
        }
        driverAdapter?.setData(listDriverFilter)
    }

    private fun requestRoute(dataDriver: DataDriver) {
            val destination = Point.fromLngLat(dataDriver.lon!!.toDouble(), dataDriver.lat!!.toDouble())
            NavigationRoute.builder(binding.root.context)
                .accessToken(getString(R.string.mapbox_access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(object : retrofit2.Callback<DirectionsResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<DirectionsResponse>,
                        response: retrofit2.Response<DirectionsResponse>
                    ) {
                        if (response.body() == null) {
                            Toast.makeText(
                                binding.root.context,
                                "No routes found, make sure you set the right user and access token.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        } else if (response.body()?.routes()?.size == 0) {
                            Toast.makeText(
                                binding.root.context,
                                "Jarak terlalu jauh dari derah danau toba",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return
                        }
                        currentRoute = response.body()?.routes()?.get(0)
                        distance = currentRoute?.distance()
                        var distanceToMeter = distance!! * 0.001
                        distanceToMeter = Math.round(distanceToMeter * 100.0) / 100.0
                        dataDriver.jarak = distanceToMeter
                        listDriver.add(dataDriver)
                        driverAdapter?.setData(listDriver)
                        binding.nestedProgress.visibility = View.GONE
                    }

                    override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable) {
                        Toast.makeText(binding.root.context, "Error : $t", Toast.LENGTH_SHORT).show()
                    }
                })

    }


    private fun showData() {
        if (lat != 0.0 && lon != 0.0) {
            origin = Point.fromLngLat(
                lon, lat
            )
        } else {
            origin = Point.fromLngLat(
                sharedPrefLogin.getUser().lon!!.toDouble(),
                sharedPrefLogin.getUser().lat!!.toDouble()
            )
        }
        binding.apply {
            orderViewModel.getKendaraan(origin.latitude(), origin.longitude()).observe(viewLifecycleOwner, { it ->
                when (it) {
                    is Resource.Success -> {
                        if (it.data?.data != null) {
                            it.data?.data.let { driver-> listDriver.addAll(driver) }
                            driverAdapter?.setData(listDriver.sortedBy { it.jarak })
                            binding.nestedProgress.visibility = View.GONE
                            if (listDriver.isEmpty()){
                                binding.layoutNotFound.visibility = View.GONE
                            }
//                            CoroutineScope(Dispatchers.Main).launch{
//                                for (data in it.data.data) {

//                                        requestRoute(data)
//                                        if (listDriver.size>=0){
//                                            driverAdapter?.setData(listDriver)
//                                            binding.nestedProgress.visibility = View.GONE
//                                        }
//                                    }
//
//                                }


                        }
                    }

                    is Resource.Loading -> {
                        nestedProgress.visibility = View.VISIBLE
                    }

                    is Resource.Error -> {
                        nestedProgress.visibility = View.GONE
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun searchDriver() {
        val key = binding.etSearch.text.toString().lowercase(Locale.getDefault())
        listDriverFilter.clear()
        for (cek in listDriver) {
            if (cek.nama!!.lowercase().contains(key) || cek.muatanKendaraan!!.lowercase()
                    .contains(key) || cek.nama!!.lowercase().contains(key)
            ) {
                listDriverFilter.add(cek)
            }
        }

        if(listDriverFilter.size<=0){
            binding.layoutNotFound.visibility = View.VISIBLE
        }else{
            binding.layoutNotFound.visibility = View.GONE
        }
        driverAdapter?.setData(listDriverFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {

                    var distanceToMeter =
                        data.getDoubleExtra(OrderDetailActivity.DISTANCE, 0.0) * 0.001
                    var duration = data.getDoubleExtra(OrderDetailActivity.DURATION, 0.0)
                    distanceToMeter = Math.round(distanceToMeter * 100.0) / 100.0
                    waktuTempuh = Math.round(duration / 60).toInt()

                    jarak = distanceToMeter
                    lat = data.getDoubleExtra(OrderMapActivity.lat, 0.0)
                    lon = data.getDoubleExtra(OrderMapActivity.lon, 0.0)
                    currenLocation =
                        data.getStringExtra(OrderDetailActivity.CURRENT_LOCATION).toString()
                    targetLocation =
                        data.getStringExtra(OrderDetailActivity.TARGET_LOCATION).toString()
                    showData()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        driverAdapter = null
    }

}