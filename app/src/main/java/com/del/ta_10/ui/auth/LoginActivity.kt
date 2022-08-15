package com.del.ta_10.ui.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.del.ta_10.MainActivity
import com.del.ta_10.MainActivityDriver
import com.del.ta_10.R
import com.del.ta_10.data.response.DataAuth
import com.del.ta_10.databinding.ActivityLoginBinding
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import com.google.gson.Gson
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var sharedPreferences: SharedPrefLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.orange_title)

        val dataIntent = intent.extras
        sharedPreferences = SharedPrefLogin(this)
        val role = dataIntent?.getString(ROLE)
        if (role == "petani"){
            binding.textView.text = "Masuk Sebagai Petani"
            binding.logo.setImageResource(R.drawable.logo_petani)
        }
        if(role == "supir"){
            binding.textView.text = "Masuk Sebagai Supir"
            binding.logo.setImageResource(R.drawable.logo_supir)
        }
        binding.apply {
            buttonLogin.setOnClickListener {
                val username = loginEmail.text.toString()
                val password = loginPassowrd.text.toString()
                if(role!=null){
                    login(username, password, role)
                }
            }
            txtDaftar.setOnClickListener {
                val intent =  Intent(this@LoginActivity, RegisterActivity::class.java)
                intent.putExtra(ROLE, role)
                startActivity(intent)
            }
        }

    }

    fun login(username: String, password: String, role: String){
        authViewModel.login(username,password,role).observe(this@LoginActivity,{
            when(it){
                is Resource.Success ->{
                    if(it.data?.status.equals("false")){
                        Toast.makeText(this@LoginActivity, it.data?.massage, Toast.LENGTH_SHORT).show()
                        failed()
                    }else{
                        if (it.data?.data?.role.equals("petani")){
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            setLoginShared(it.data?.data)
                            startActivity(intent)
                            finish()
                        }

                        if(it.data?.data?.role.equals("supir")){
                            val intent = Intent(this@LoginActivity, MainActivityDriver::class.java)
                            setLoginShared(it.data?.data)
                            startActivity(intent)
                            finish()
                        }

                    }
                }
                is Resource.Error ->{
                    Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                    failed()
                }
                is Resource.Loading->{
                    loading()
                }
            }
        })
    }

    private fun setLoginShared(dataAuth: DataAuth?){
        sharedPreferences.setStatusLogin(true)
        Toast.makeText(this@LoginActivity, sharedPreferences.getStatusLogin().toString(), Toast.LENGTH_SHORT)
            .show()
        val dt = Gson().toJson(dataAuth)
        sharedPreferences.setUser(dt)
    }

    private fun loading(){
        binding.apply {
            nestedProgress.visibility = View.VISIBLE
            loadingBg.visibility = View.VISIBLE
            buttonLogin.isEnabled = false
        }
    }

    private fun failed(){
        binding.apply {
            nestedProgress.visibility = View.GONE
            loadingBg.visibility = View.GONE
            buttonLogin.isEnabled = true
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val ROLE = "role"
    }
}