package com.del.ta_10.data.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("data")
	val data: DataAuth?= null,

	@field:SerializedName("massage")
	val massage: String? = null,

	@field:SerializedName("errors")
	val errors: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataAuth(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("poto_profil")
	val potoProfil: Any? = null,

	@field:SerializedName("nama")
	val namaPetani: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val idPetani: Int = 3,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("no_telepon")
	val noTelepon: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("jenis_kendaraan")
	val jenis_kendaraan: String? = null,

	@field:SerializedName("muatan_kendaraan")
	val muatan_kendaraan: String? = null,

	@field:SerializedName("muatan_terpenuhi")
	var muatan_terpenuhi: String? = null,

	@field:SerializedName("stat")
	var stat: String? = null,

	@field:SerializedName("id_kendaraan")
	val id_kendaraan: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("no_tf")
	val no_tf: String? = null


)
