package com.del.ta_10.domain.model

import com.del.ta_10.data.response.DataAuth
import com.google.gson.annotations.SerializedName

data class Auth(
    val statusCode: Int? = null,
    val data: DataAuth? = DataAuth(),
    val massage: String? = null,
    val errors: Any? = null,
    val status: String? = null
)

