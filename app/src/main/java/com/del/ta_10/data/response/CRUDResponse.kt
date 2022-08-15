package com.del.ta_10.data.response

import com.google.gson.annotations.SerializedName

data class CRUDResponse(

	@field:SerializedName("massage")
	val massage: String? = null,

	@field:SerializedName("errors")
	val errors: Any? = null,

	@field:SerializedName("content")
	val content: Any? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("user")
	val user: DataAuth? = null,
)

