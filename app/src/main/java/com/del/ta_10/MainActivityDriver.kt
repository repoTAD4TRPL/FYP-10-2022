package com.del.ta_10


import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.del.ta_10.databinding.ActivityMainDriverBinding
import com.del.ta_10.util.SharedPrefLogin

class MainActivityDriver : AppCompatActivity() {
    private lateinit var binding: ActivityMainDriverBinding
    private lateinit var sharedPreferences: SharedPrefLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataIntent = intent.extras

        val navView: BottomNavigationView = binding.navView
        sharedPreferences = SharedPrefLogin(this)

        supportActionBar?.hide()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = Color.WHITE

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val navController = findNavController(R.id.nav_host_fragment_activity_main_driver)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }
}