package com.del.ta_10.domain.usecase

import com.del.ta_10.data.TaRepository
import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.domain.model.*
import com.del.ta_10.vo.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TaInteractor(private  val taRepository: TaRepository): TaUseCase {
    override fun login(username: String, password: String, role: String): Flow<Resource<Auth>> {
        return taRepository.login(username, password, role)
    }

    override fun hasilTaniByUser(id: Int): Flow<Resource<HasilTani>> {
        return taRepository.hasilTaniByUser(id)
    }

    override fun getHasilTaniAll(): Flow<Resource<HasilTani>> {
        return taRepository.getHasilTaniAll()
    }


    override fun tambahHasilTani(
        img: MultipartBody.Part?,
        text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Flow<Resource<CRUD>> {
        return taRepository.tambahHasilTani(img, text)
    }

    override fun listOrderByUser(id: Int, role: String): Flow<Resource<Order>> {
        return taRepository.listOrderByUser(id, role)
    }

    override fun allKendaraan(lat: Double, long:Double): Flow<Resource<Kendaraan>> {
        return taRepository.allKendaraan(lat, long)
    }

    override fun orderKendaraan(
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
    ): Flow<Resource<CRUD>> {
        return taRepository.orderKendaraan(alamat, id_kendaraan, id_petani, status_pembayaran, bukti_pembayaran, berat_bawaan, harga, status, lokasi_awal, lokasi_tujuan, tgl_penjemputan, durasi_pengataran)
    }


}