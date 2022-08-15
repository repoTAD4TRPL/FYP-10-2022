package com.del.ta_10.data.response

import com.google.gson.annotations.SerializedName

data class PelabuhanResponse(
	@field:SerializedName("data")
	val pelabuhan: List<Pelabuhans>? = null,
	@field:SerializedName("massage")
	val massage: String? = null,
	@field:SerializedName("status")
	val status: String? = null
)

data class Pelabuhans(
	@field:SerializedName("v")
	val namaPelabuhan: String? = null,
	@field:SerializedName("lon")
	val lon: Double? = null,
	@field:SerializedName("id")
	val id: Int? = null,
	@field:SerializedName("lat")
	val lat: Double? = null
)

