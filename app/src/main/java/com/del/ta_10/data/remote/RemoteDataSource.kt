package com.del.ta_10.data.remote

import android.util.Log
import com.del.ta_10.data.network.ApiResponse
import com.del.ta_10.data.network.ApiService
import com.del.ta_10.data.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import java.lang.Exception

class RemoteDataSource constructor(private val apiService: ApiService) {
    fun login(
        username: String,
        password: String,
        role: String
    ): Flow<ApiResponse<AuthResponse>> {
        return flow {
            try {
                val data = apiService.login(username, password, role)
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun hasilTaniByUser(id: Int): Flow<ApiResponse<HasilTaniResponse>> {
        return flow {
            try {
                val data = apiService.getHasilTaniUser(id)
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun hasilTaniAll(): Flow<ApiResponse<HasilTaniResponse>> {
        return flow {
            try {
                val data = apiService.getHasilTaniAll()
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun tambahHasilTani(
        img: MultipartBody.Part?,
        text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Flow<ApiResponse<CRUDResponse>> {
        return flow {
            try {
                val data =
                    apiService.tambahHasilTani(img, text)
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun listOrderByUser(id: Int, role: String): Flow<ApiResponse<OrderResponse>> {
        return flow {
            try {
                val data = apiService.listOrderByUser(id, role)
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun allKendaraan(lat: Double, long:Double): Flow<ApiResponse<KendaraanResponse>> {
        return flow {
            try {
                val data = apiService.allKendaraan(lat, long)
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

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
    ): Flow<ApiResponse<CRUDResponse>> {
        return flow {
            try {
                val data = apiService.orderKendaraan(alamat, id_kendaraan, id_petani, status_pembayaran, bukti_pembayaran, berat_bawaan, harga, status, lokasi_awal, lokasi_tujuan, tgl_penjemputan, durasi_pengataran)
                emit(ApiResponse.Success(data))
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.d(RemoteDataSource::class.java.simpleName, e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}