package com.del.ta_10.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class HasilTaniResponse(

	@field:SerializedName("massage")
	val massage: String? = null,

	@field:SerializedName("data")
	val data: List<DataTani>? = null,

	@field:SerializedName("errors")
	val errors: Any? = null,

	@field:SerializedName("content")
	val content: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)


@Parcelize
data class DataTani(

	@field:SerializedName("harga")
	var harga: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("berat_barang")
	var beratBarang: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_petani")
	val idPetani: Int? = null,

	@field:SerializedName("id_hasil_panen")
	val idHasilPanen: Int? = null,

	@field:SerializedName("tanggal_post")
	val tanggalPost: String? = null,

	@field:SerializedName("jenis_hasil_tani")
	var jenisHasilTani: String? = null,

	@field:SerializedName("poto_profil")
	val potoProfil: String? = null,

	@field:SerializedName("nama")
	val namaPetani: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("no_telepon")
	val noTelepon: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("img")
	var img: String? = null,
): Parcelable
