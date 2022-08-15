package com.del.ta_10.domain.model

import com.del.ta_10.data.response.DataOrder

data class Order (
    val data: List<DataOrder>? = null,
    val massage: String? = null,
    val errors: Any? = null,
    val content: Any? = null,
    val status: String? = null
)