package com.del.ta_10.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.del.ta_10.R
import com.del.ta_10.adapter.ProductAdapter
import com.del.ta_10.databinding.FragmentDashboardBinding
import com.del.ta_10.ui.auth.AuthViewModel
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var _binding: FragmentDashboardBinding? = null
    private var productAdapter: ProductAdapter? = null
    lateinit var sharedPref: SharedPrefLogin

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var role = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sharedPref = SharedPrefLogin(binding.root.context)

        if (arguments!=null){
            role = requireArguments().getString(ROLE).toString()
        }

        productAdapter = ProductAdapter(role)
        if (role=="pembeli"){
            binding.fab.visibility = View.GONE
        }

        with(binding.rvProduct){
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }

        binding.fab.setOnClickListener {
            val intent = Intent(binding.root.context, AddProductActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (role!="pembeli"){
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
        }else{
            dashboardViewModel.getProductAll().observe(this, {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        productAdapter = null
    }

    companion object{
        const val ROLE="role"
    }
}