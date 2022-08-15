package com.del.ta_10.domain.usecase

import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.domain.model.*
import com.del.ta_10.vo.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface TaUseCase {
    fun login(username: String, password: String, role: String): Flow<Resource<Auth>>

    fun hasilTaniByUser(id: Int): Flow<Resource<HasilTani>>

    fun getHasilTaniAll(): Flow<Resource<HasilTani>>

    fun tambahHasilTani(
        img: MultipartBody.Part?,
        text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Flow<Resource<CRUD>>

    fun listOrderByUser(id: Int, role: String): Flow<Resource<Order>>

    fun allKendaraan(lat: Double, long:Double): Flow<Resource<Kendaraan>>

    fun orderKendaraan(
        alamat: String,
        id_kendaraan: String?,
        id_petani: String,
        status_pembayaran: String,
        bukti_pembayaran: String,
        berat_bawaan: String,
        harga: String,
        status: String,
        lokasi_awal: String,
        lokasi_tujuan: String,
        tgl_penjemputan: String,
        durasi_pengataran: String
    ): Flow<Resource<CRUD>>
}