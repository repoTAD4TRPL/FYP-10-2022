package com.del.ta_10.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.del.ta_10.domain.usecase.TaUseCase

class OrderViewModel(private val taUseCase: TaUseCase) : ViewModel() {
    fun getKendaraan(lat: Double, long:Double) = taUseCase.allKendaraan(lat, long).asLiveData()

    fun orderKendaraan(
        alamat: String,
        id_kendaraan: String,
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
    ) = taUseCase.orderKendaraan(
        alamat,
        id_kendaraan,
        id_petani,
        status_pembayaran,
        bukti_pembayaran,
        berat_bawaan,
        harga,
        status,
        lokasi_awal,
        lokasi_tujuan,
        tgl_penjemputan,
        durasi_pengataran
    )
        .asLiveData()
}