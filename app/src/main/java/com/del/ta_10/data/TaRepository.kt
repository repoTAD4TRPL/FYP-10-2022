package com.del.ta_10.data

import com.del.ta_10.data.network.ApiResponse
import com.del.ta_10.data.remote.RemoteDataSource
import com.del.ta_10.data.response.*
import com.del.ta_10.domain.model.*
import com.del.ta_10.domain.repository.ITaRepository
import com.del.ta_10.util.DataMapper
import com.del.ta_10.vo.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class TaRepository constructor(private  val remoteDataSource: RemoteDataSource): ITaRepository {
    override fun login(username: String, password: String, role: String): Flow<Resource<Auth>> {
        return object: NetworkBoundResource<Auth, AuthResponse>(){
            override fun loadFromNetwork(data: AuthResponse): Flow<Auth> {
                return DataMapper.mapResponsesAuthToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<AuthResponse>> {
                return remoteDataSource.login(username, password, role)
            }

        }.asFlow()
    }

    override fun hasilTaniByUser(id: Int): Flow<Resource<HasilTani>> {
        return object: NetworkBoundResource<HasilTani, HasilTaniResponse>(){
            override fun loadFromNetwork(data: HasilTaniResponse): Flow<HasilTani> {
                return DataMapper.mapResponseHasilTani(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<HasilTaniResponse>> {
                return remoteDataSource.hasilTaniByUser(id)
            }

        }.asFlow()
    }

    override fun getHasilTaniAll(): Flow<Resource<HasilTani>> {
        return object: NetworkBoundResource<HasilTani, HasilTaniResponse>(){
            override fun loadFromNetwork(data: HasilTaniResponse): Flow<HasilTani> {
                return DataMapper.mapResponseHasilTani(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<HasilTaniResponse>> {
                return remoteDataSource.hasilTaniAll()
            }

        }.asFlow()
    }

    override fun tambahHasilTani(
        img: MultipartBody.Part?,
        text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Flow<Resource<CRUD>> {
        return object: NetworkBoundResource<CRUD, CRUDResponse>(){
            override fun loadFromNetwork(data: CRUDResponse): Flow<CRUD> {
                return DataMapper.mapRespobseCRUD(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<CRUDResponse>> {
                return remoteDataSource.tambahHasilTani(img, text)
            }
        }.asFlow()
    }

    override fun listOrderByUser(id: Int, role: String): Flow<Resource<Order>> {
        return object : NetworkBoundResource<Order, OrderResponse>(){
            override fun loadFromNetwork(data: OrderResponse): Flow<Order> {
                return DataMapper.mapResponseOrder(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<OrderResponse>> {
                return remoteDataSource.listOrderByUser(id, role)
            }
        }.asFlow()
    }

    override fun allKendaraan(lat: Double, long:Double): Flow<Resource<Kendaraan>> {
        return object : NetworkBoundResource<Kendaraan, KendaraanResponse>(){
            override fun loadFromNetwork(data: KendaraanResponse): Flow<Kendaraan> {
                return DataMapper.mapResponseKendaraan(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<KendaraanResponse>> {
                return remoteDataSource.allKendaraan(lat, long)
            }
        }.asFlow()
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
        return object : NetworkBoundResource<CRUD, CRUDResponse>(){
            override fun loadFromNetwork(data: CRUDResponse): Flow<CRUD> {
                return  DataMapper.mapRespobseCRUD(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<CRUDResponse>> {
                return remoteDataSource.orderKendaraan(alamat, id_kendaraan, id_petani, status_pembayaran, bukti_pembayaran, berat_bawaan, harga, status, lokasi_awal, lokasi_tujuan, tgl_penjemputan, durasi_pengataran)
            }

        }.asFlow()
    }

}