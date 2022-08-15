package com.del.ta_10.data.response

import com.google.gson.annotations.SerializedName

data class LokasiResponse(

	@field:SerializedName("data")
	val data: List<DataLokasi>,

	@field:SerializedName("massage")
	val massage: String? = null,

	@field:SerializedName("content")
	val content: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataLokasi(

	@field:SerializedName("id_order")
	val idOrder: Int? = null,

	@field:SerializedName("lan")
	val lan: Double? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("lokasi_saat_ini")
	val lokasiSaatIni: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
)
