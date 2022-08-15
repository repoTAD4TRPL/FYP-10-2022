package com.del.ta_10.ui.order

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.del.ta_10.R
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.PelabuhanResponse
import com.del.ta_10.data.response.Pelabuhans
import com.del.ta_10.util.helper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class OrderMapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var btnNavigation: Button
    private lateinit var mapboxMap: MapboxMap

    private lateinit var symbolManager: SymbolManager
    private lateinit var mySymbolManager: SymbolManager
    private lateinit var locationComponent: LocationComponent
    private lateinit var mylocation: LatLng
    private lateinit var targetLocation: LatLng
    private lateinit var permissionsManager: PermissionsManager

    private lateinit var navigationMapRoute: NavigationMapRoute
    private var currentRoute: DirectionsRoute? = null
    var distance: Double? = 0.0
    var duration: Double? = 0.0
    var cek = 0
    var listPelabuhan = ArrayList<Pelabuhans>()
    private lateinit var smallestDistance: Pelabuhans

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_map)
        supportActionBar?.hide()

        val extra = intent.extras

        mapView = findViewById(R.id.mapView)
        btnNavigation = findViewById(R.id.btnNavigation)
        mylocation = LatLng(
            0.0, 0.0
        )
        targetLocation = LatLng(
            0.0, 0.0
        )

        getPelabuhanList()

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                symbolManager = SymbolManager(mapView, mapboxMap, style)
                symbolManager.iconAllowOverlap = true
                style.addImage(
                    ICON_ID,
                    BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_default)
                )

                mySymbolManager = SymbolManager(mapView, mapboxMap, style)
                mySymbolManager.iconAllowOverlap = true


                navigationMapRoute = NavigationMapRoute(
                    null,
                    mapView,
                    mapboxMap,
                    R.style.NavigationMapRoute
                )

                if (extra?.getBoolean(PILIH_SENDIRI) == false){
                    showMyLocation(style)
                    addMarkerOnClickByDevice()
                }else{
                    zoomLocation()
                    addMarkerOnClick()
                }

            }
        }


    }

    private fun requestRoute(origin: Point, destination: Point) {
        navigationMapRoute.updateRouteVisibilityTo(false)
        NavigationRoute.builder(this)
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
                            this@OrderMapActivity,
                            "No routes found, make sure you set the right user and access token.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    } else if (response.body()?.routes()?.size == 0) {
                        Toast.makeText(
                            this@OrderMapActivity,
                            "No routes found.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return
                    }

                    currentRoute = response.body()?.routes()?.get(0)

                    navigationMapRoute.addRoute(currentRoute)
                    distance = currentRoute?.distance()
                    duration = currentRoute?.duration()

                }

                override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(this@OrderMapActivity, "Error : $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun getAddress(lat: Double, lng: Double): String {
        var data = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(lat, lng, 1)
            val obj: Address = addresses[0]
            var add: String = obj.getAddressLine(0)
            add = """
            $add
            ${obj.getCountryName()}
            """.trimIndent()
            add = """
            $add
            ${obj.getCountryCode()}
            """.trimIndent()
            add = """
            $add
            ${obj.getAdminArea()}
            """.trimIndent()
            add = """
            $add
            ${obj.getPostalCode()}
            """.trimIndent()
            add = """
            $add
            ${obj.getSubAdminArea()}
            """.trimIndent()
            add = """
            $add
            ${obj.getLocality()}
            """.trimIndent()
            add = """
            $add
            ${obj.getSubThoroughfare()}
            """.trimIndent()
            data =
                "${obj.countryName}, ${obj.adminArea}, ${obj.adminArea}, ${obj.postalCode}, ${obj.locality}"
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

        return data
    }

    private fun addMarkerOnClick() {

        var isMy = false
        var cekFirst = true
        mapboxMap.addOnMapClickListener { point ->
//            symbolManager.deleteAll()
//            symbolManager.create(
//                SymbolOptions()
//                    .withLatLng(LatLng(point.latitude, point.longitude))
//                    .withIconImage(ICON_ID)
//                    .withDraggable(false)
//            )

            mySymbolManager.addClickListener {
                isMy = true
                cek = 0
            }

            symbolManager.addClickListener {
                isMy = false
                cek = 1
            }

            if (cek == 0) {
                mySymbolManager.deleteAll()

                mySymbolManager.create(
                    SymbolOptions()
                        .withLatLng(LatLng(point.latitude, point.longitude))
                        .withIconImage(ICON_ID)
                        .withTextField("Lokasi Awal")
                )
                mylocation = LatLng(
                    point.latitude, point.longitude
                )
                if (!isMy) {
                    cek++
                }
            }


            if (cek == 1 && !cekFirst) {
                symbolManager.deleteAll()
                symbolManager.create(
                    SymbolOptions()
                        .withLatLng(LatLng(point.latitude, point.longitude))
                        .withIconImage(ICON_ID)
                        .withTextField("Lokasi Tujuan")
                )
                targetLocation = LatLng(
                    point.latitude, point.longitude
                )


                btnNavigation.visibility = View.VISIBLE
            }
            cekFirst = false

            if (mylocation.latitude != 0.0 && targetLocation.latitude != 0.0) {
                val destination = Point.fromLngLat(targetLocation.longitude, targetLocation.latitude)
                val origin = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
                requestRoute(origin, destination)
            }

            btnNavigation.setOnClickListener {
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra(
                    OrderDetailActivity.TARGET_LOCATION,
                    getAddress(point.latitude, point.longitude)
                )
                intent.putExtra(
                    OrderDetailActivity.CURRENT_LOCATION,
                    getAddress(mylocation.latitude, mylocation.longitude)
                )
                intent.putExtra(lat, mylocation.latitude)
                intent.putExtra(lon, mylocation.longitude)
                intent.putExtra(OrderDetailActivity.DISTANCE, distance)
                intent.putExtra(OrderDetailActivity.DURATION, duration)
                setResult(RESULT_OK, intent)
                finish()
//            val simulateRoute = true
//
//            val options = NavigationLauncherOptions.builder()
//                .directionsRoute(currentRoute)
//                .shouldSimulateRoute(simulateRoute)
//                .build()
//
//            NavigationLauncher.startNavigation(this, options)
            }

            true
        }
    }

    private fun addMarkerOnClickByDevice() {
        mapboxMap.addOnMapClickListener { point ->
            symbolManager.deleteAll()
            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(point.latitude, point.longitude))
                    .withIconImage(ICON_ID)
                    .withDraggable(false)
            )

                val destination = Point.fromLngLat(point.longitude, point.latitude)
                val origin = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
                requestRoute(origin, destination)

            btnNavigation.visibility = View.VISIBLE
            btnNavigation.setOnClickListener {
                val intent = Intent(this, OrderDetailActivity::class.java)
                intent.putExtra(
                    OrderDetailActivity.TARGET_LOCATION,
                    getAddress(point.latitude, point.longitude)
                )
                intent.putExtra(
                    OrderDetailActivity.CURRENT_LOCATION,
                    getAddress(mylocation.latitude, mylocation.longitude)
                )
                intent.putExtra(OrderDetailActivity.DISTANCE, distance)
                intent.putExtra(OrderDetailActivity.DURATION, duration)
                setResult(RESULT_OK, intent)
                finish()
//            val simulateRoute = true
//
//            val options = NavigationLauncherOptions.builder()
//                .directionsRoute(currentRoute)
//                .shouldSimulateRoute(simulateRoute)
//                .build()
//
//            NavigationLauncher.startNavigation(this, options)
            }

            true
        }
    }


    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            val locationComponentOptions = LocationComponentOptions.builder(this)
                .pulseEnabled(true)
                .pulseColor(Color.BLUE)
                .pulseAlpha(.4f)
                .pulseInterpolator(BounceInterpolator())
                .build()
            val locationComponentActivationOptions = LocationComponentActivationOptions
                .builder(this, style)
                .locationComponentOptions(locationComponentOptions)
                .build()
            locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
            mylocation = LatLng(
                locationComponent.lastKnownLocation?.latitude as Double,
                locationComponent.lastKnownLocation?.longitude as Double
            )

                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 12.0))
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                    Toast.makeText(
                        this@OrderMapActivity,
                        "Anda harus mengizinkan location permission untuk menggunakan aplikasi ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        mapboxMap.getStyle { style ->
                            showMyLocation(style)
                        }
                    } else {
                        finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun zoomLocation(){
            val locationManager: LocationManager =
                this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers: List<String> = locationManager.getProviders(true)
            var location: Location? = null
            for (i in providers.size - 1 downTo 0) {
                location = locationManager.getLastKnownLocation(providers[i])
                if (location != null)
                    break
            }
            if (location != null) {
                var  loc = LatLng(
                    location.latitude, location.longitude
                )
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0))

            }
    }

    fun getPelabuhanList(){
        RetrofitClient.apiInstance.getPelabuhan()
            .enqueue(object : Callback<PelabuhanResponse> {
                override fun onResponse(
                    call: Call<PelabuhanResponse>,
                    response: Response<PelabuhanResponse>
                ) {
                    if (response.isSuccessful){
                        response.body()?.pelabuhan?.let { listPelabuhan.addAll(it) }
                    }
                }

                override fun onFailure(call: Call<PelabuhanResponse>, t: Throwable) {
                    Log.d("ERROR", t.message.toString())
                }

            })

    }

    private fun getRouteDistance(origin: Point, destination: Point){
        NavigationRoute.builder(this)
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
                            this@OrderMapActivity,
                            "No routes found, make sure you set the right user and access token.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    } else if (response.body()?.routes()?.size == 0) {
                        Toast.makeText(
                            this@OrderMapActivity,
                            "No routes found.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return
                    }

                }

                override fun onFailure(call: retrofit2.Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(this@OrderMapActivity, "Error : $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val ICON_ID = "ICON_ID"
        const val PILIH_SENDIRI = "PILIH_SENDIRI"
        const val lat = "lat"
        const val lon = "lon"
    }

}