package com.del.ta_10.domain.model

import com.google.gson.annotations.SerializedName

data class CRUD (
    val massage: String? = null,
    val errors: Any? = null,
    val content: Any? = null,
    val status: String? = null
)