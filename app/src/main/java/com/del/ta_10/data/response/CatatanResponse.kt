package com.del.ta_10.data.response

import com.google.gson.annotations.SerializedName

data class CatatanResponse(

	@field:SerializedName("data")
	val data: List<Catatan>? = null,

	@field:SerializedName("massage")
	val massage: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Catatan(

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("poto_profil")
	val potoProfil: String? = null,

	@field:SerializedName("id_pengaju")
	val idPengaju: Int? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_order_kendaraan")
	val idOrderKendaraan: Int? = null,

	@field:SerializedName("pengaju")
	val pengaju: String? = null
)
