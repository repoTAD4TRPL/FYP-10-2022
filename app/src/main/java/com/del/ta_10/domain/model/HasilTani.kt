package com.del.ta_10.domain.model

import com.del.ta_10.data.response.DataTani
import com.google.gson.annotations.SerializedName


data class HasilTani(
    val statusCode: Int? = null,
    val data: List<DataTani>? = null,
    val massage: String? = null,
    val errors: Any? = null,
    val status: String? = null,
    val potoProfil: Any? = null,
    val namaPetani: String? = null,
    val username: String? = null,
    val noTelepon: String? = null,
    val alamat: Any? = null
)
