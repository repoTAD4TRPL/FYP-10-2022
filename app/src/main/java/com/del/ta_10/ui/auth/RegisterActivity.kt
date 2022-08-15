package com.del.ta_10.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.del.ta_10.R
import com.del.ta_10.data.network.ApiService
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.AuthResponse
import com.del.ta_10.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var _binding : ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dataIntent = intent.extras
        val role = dataIntent?.getString(LoginActivity.ROLE)

        supportActionBar?.hide()
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            txtLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            if (role=="petani"){
                titleRegister.text = "Daftar Akun Petani"
            }else{
                titleRegister.text = "Daftar Akun Supir"
            }
            btnRegister.setOnClickListener {
                val username = edtUsername.text.toString()
                val name = edtNama.text.toString()
                val noHp = edtNoHp.text.toString()
//                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val c_password = edtRePassword.text.toString()

                RetrofitClient.apiInstance.register(username, password, c_password, noHp, name, role)
                    .enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(
                            call: Call<AuthResponse>,
                            response: Response<AuthResponse>
                        ) {
                            if (response.isSuccessful){
                                Toast.makeText(this@RegisterActivity, response.body()?.massage, Toast.LENGTH_SHORT).show()
                                if(response.body()?.status=="ok"){
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    intent.putExtra(LoginActivity.ROLE, role)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }

                        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                            Toast.makeText(this@RegisterActivity, "Error ${t.message}", Toast.LENGTH_SHORT).show()
                        }

                    })

            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}