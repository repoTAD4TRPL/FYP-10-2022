package com.del.ta_10.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.text.Editable
import android.widget.Toast
import androidx.annotation.NonNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.text.NumberFormat
import java.util.*

class helper {

    fun getAddress(lat: Double, lng: Double, context: Context): String {
        var data = ""
        val geocoder = Geocoder(context, Locale.getDefault())
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
            add = """
                $add
                ${obj.getAddressLine(0)}
                """.trimIndent()
            data =
                "${obj.getAddressLine(0)}"
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        return data
    }

    fun convertRupiah(money: Int?): String {
        val localId = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localId)
        val strFormat = formatter.format(money)
        return strFormat
    }

}