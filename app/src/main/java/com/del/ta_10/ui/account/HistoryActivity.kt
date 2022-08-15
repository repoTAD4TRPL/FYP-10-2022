package com.del.ta_10.ui.account

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.del.ta_10.R
import com.del.ta_10.adapter.OrderAdapter
import com.del.ta_10.data.response.DataAuth
import com.del.ta_10.data.response.DataDriver
import com.del.ta_10.data.response.DataOrder
import com.del.ta_10.databinding.ActivityHistoryBinding
import com.del.ta_10.ui.home.HomeViewModel
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var user: DataAuth
    private lateinit var sharedPref: SharedPrefLogin
    var listStory = ArrayList<DataOrder>()
    var listStoryFillter = ArrayList<DataOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPref = SharedPrefLogin(binding.root.context)
        orderAdapter = OrderAdapter()

        user = sharedPref.getUser()

        val spineer = binding.filterStory
        val listFilter = resources.getStringArray(R.array.filter_story)
        if (spineer != null) {
            val adapter =
                ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, listFilter)
            spineer.adapter = adapter
        }

        spineer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                listStoryFillter.clear()
                var cek =listFilter[position]
                if (listFilter[position]=="Ditolak"){
                    cek = "tolak"
                }
                if(listFilter[position]=="Batal"){
                    cek = "batalkan"
                }
                if (cek.equals("Pilih Status")) {
                    listStoryFillter.addAll(listStory)
                } else {
                    for(story in listStory){
                        if (story.status.equals(cek)){
                            listStoryFillter.add(story)
                        }
                    }
                }
                orderAdapter.setData(listStoryFillter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }


        binding.nestedProgress.visibility = View.VISIBLE
        setDataOrder()
        with(binding.rvOrder) {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = orderAdapter
        }
    }

    private fun setDataOrder() {
        binding.apply {
            user.idPetani?.let {
                user.role?.let { it1 ->
                    homeViewModel.getDummyOrder(it, it1).observe(this@HistoryActivity, {
                        when (it) {
                            is Resource.Success -> {
                                nestedProgress.visibility = View.GONE
                                orderAdapter.setData(it.data?.data)
                                orderAdapter.usr(user.role)
                                it.data?.data?.let { it2 -> listStory.addAll(it2) }
                            }
                            is Resource.Loading -> {
                                nestedProgress.visibility = View.VISIBLE
                            }
                            is Resource.Error -> {
                                Toast.makeText(this@HistoryActivity, it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                }
            }
        }
    }


}