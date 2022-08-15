package com.del.ta_10.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.del.ta_10.MainActivity
import com.del.ta_10.databinding.ActivityMenuAuthBinding
import com.del.ta_10.ui.pembeli.PembeliActivity
import com.del.ta_10.util.SharedPrefLogin

class MenuAuthActivity : AppCompatActivity() {
    private var _binding: ActivityMenuAuthBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefLogin: SharedPrefLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMenuAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefLogin = SharedPrefLogin(this)

        supportActionBar?.hide()


        binding.apply {
            cardViewFarmer.setOnClickListener {
                if (sharedPrefLogin.getStatusLogin() && sharedPrefLogin.getUser().role.equals("petani")) {
                    val intent = Intent(this@MenuAuthActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    move("petani")
                }
            }

            cardViewDriver.setOnClickListener {
                if (sharedPrefLogin.getStatusLogin() && sharedPrefLogin.getUser().role.equals("supir")) {
                    val intent = Intent(this@MenuAuthActivity, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    move("supir")
                }
            }

            cardViewBuyer.setOnClickListener {
                val intent = Intent(this@MenuAuthActivity, PembeliActivity::class.java)
                intent.putExtra(LoginActivity.ROLE, "pembeli")
                startActivity(intent)
            }
        }
    }

    private fun move(role: String){
        val intent = Intent(this@MenuAuthActivity, LoginActivity::class.java)
        intent.putExtra(LoginActivity.ROLE, role)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}