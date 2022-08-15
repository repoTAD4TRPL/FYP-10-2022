package com.del.ta_10

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.BounceInterpolator
import android.widget.Toast
import com.del.ta_10.databinding.ActivitySplashScreenBinding
import com.del.ta_10.ui.auth.LoginActivity
import com.del.ta_10.ui.auth.MenuAuthActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat




@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var permissionsManager: PermissionsManager

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            binding.buildVersion.text = BUILD_VERSION
//            val handler = Handler(mainLooper)
//            handler.postDelayed({
//                val intent = Intent(this, MenuAuthActivity::class.java)
//                startActivity(intent)
//                finish()
//            }, SPLASH_DELAY)
//        } else {
//            permissionsManager = PermissionsManager(object : PermissionsListener {
//                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
//                    Toast.makeText(
//                        this@SplashScreenActivity,
//                        "Anda harus mengizinkan location permission untuk menggunakan aplikasi ini",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onPermissionResult(granted: Boolean) {
//                    println("cell")
//                    binding.buildVersion.text = BUILD_VERSION
//                    val handler = Handler(mainLooper)
//                    handler.postDelayed({
//                        val intent = Intent(this@SplashScreenActivity, MenuAuthActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }, SPLASH_DELAY)
//                }
//            })
//            permissionsManager.requestLocationPermissions(this)
//        }
        binding.buildVersion.text = BUILD_VERSION
        if (checkPermission()){
                    val handler = Handler(mainLooper)
                    handler.postDelayed({
                        val intent = Intent(this@SplashScreenActivity, MenuAuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, SPLASH_DELAY)
        }else {
            if (ContextCompat.checkSelfPermission(
                    this@SplashScreenActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !==
                PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@SplashScreenActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        this@SplashScreenActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this@SplashScreenActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                    )
                }
            }
        }
    }
    private fun checkPermission(): Boolean {
        //int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        val result1 = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        val handler = Handler(mainLooper)
                        handler.postDelayed({
                            val intent = Intent(this@SplashScreenActivity, MenuAuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, SPLASH_DELAY)
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    if (ContextCompat.checkSelfPermission(
                            this@SplashScreenActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) !==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this@SplashScreenActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        ) {
                            ActivityCompat.requestPermissions(
                                this@SplashScreenActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                            )
                        } else {
                            ActivityCompat.requestPermissions(
                                this@SplashScreenActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                            )
                        }
                    }
                }
                return
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        private const val SPLASH_DELAY = 3000L
        private const val BUILD_VERSION = BuildConfig.VERSION_NAME
    }
}