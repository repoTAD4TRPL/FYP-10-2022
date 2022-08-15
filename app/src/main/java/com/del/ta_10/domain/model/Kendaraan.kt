package com.del.ta_10.domain.model

import com.del.ta_10.data.response.DataDriver

data class Kendaraan(
    val data: List<DataDriver>?,
    val massage: String? = null,
    val errors: String? = null,
    val content: String? = null,
    val status: String? = null
)
