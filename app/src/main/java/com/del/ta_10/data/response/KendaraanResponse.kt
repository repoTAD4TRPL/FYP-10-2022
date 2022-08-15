package com.del.ta_10.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class KendaraanResponse(

	@field:SerializedName("data")
	val data: List<DataDriver>? = null,

	@field:SerializedName("massage")
	val massage: String? = null,

	@field:SerializedName("errors")
	val errors: Any? = null,

	@field:SerializedName("content")
	val content: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataDriver(

	@field:SerializedName("id_supir")
	val idSupir: Int? = null,

	@field:SerializedName("stat")
	val stat: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("muatan_kendaraan")
	val muatanKendaraan: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_kendaraan")
	val idKendaraan: Int? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("poto_profil")
	val potoProfil: String? = null,

	@field:SerializedName("jenis_kendaraan")
	val jenisKendaraan: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("no_telepon")
	val noTelepon: String? = null,

	@field:SerializedName("muatan_terpenuhi")
	val muatan_terpenuhi: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("jarak")
	var jarak: Double
) : Parcelable
