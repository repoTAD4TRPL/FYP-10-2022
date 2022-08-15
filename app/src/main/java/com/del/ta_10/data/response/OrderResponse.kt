package com.del.ta_10.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class OrderResponse(

	@field:SerializedName("data")
	val data: List<DataOrder>? = null,

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
data class DataOrder(

	@field:SerializedName("id_supir")
	val idSupir: Int? = null,

	@field:SerializedName("stat")
	val stat: String? = null,

	@field:SerializedName("muatan_kendaraan")
	val muatanKendaraan: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_kendaraan")
	val idKendaraan: Int? = null,

	@field:SerializedName("berat_bawaan")
	val beratBawaan: Int? = null,

	@field:SerializedName("username_supir")
	val usernameSupir: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("lokasi_awal")
	val lokasiAwal: String? = null,

	@field:SerializedName("id_order")
	val idOrder: Int? = null,

	@field:SerializedName("harga")
	val harga: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("status_pembayaran")
	val statusPembayaran: String? = null,

	@field:SerializedName("id_petani")
	val idPetani: Int? = null,

	@field:SerializedName("jenis_kendaraan")
	val jenisKendaraan: String? = null,

	@field:SerializedName("bukti_pembayaran")
	val buktiPembayaran: String? = null,

	@field:SerializedName("nama_supir")
	val namaSupir: String? = null,

	@field:SerializedName("lokasi_tujuan")
	val lokasiTujuan: String? = null,

	@field:SerializedName("nama_petani")
	val namaPetani: String? = null,

	@field:SerializedName("telepon")
	val telepon: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("pengaju")
	val pengaju: String? = null,

	@field:SerializedName("status_pengajuan")
	val status_pengajuan: String? = null,

	@field:SerializedName("bukti_selesai")
	val bukti_selesai: String? = null,

	@field:SerializedName("konfirmasi_selesai")
	val konfirmasi_selesai: String? = null,

	@field:SerializedName("catatan")
	val catatan: String? = null,

	@field:SerializedName("harga_awal")
	val harga_awal: Int? = null,

	@field:SerializedName("no_tf")
	val no_tf: String? = null
): Parcelable
