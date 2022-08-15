package com.del.ta_10.data.network

import com.del.ta_10.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("role") role: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("c_password") c_password: String?,
        @Field("no_telepon") no_telepon: String?,
        @Field("nama") nama: String?,
        @Field("role") role: String?,
    ): Call<AuthResponse>

    //HasilTani

    @GET("hasilTani/user/{id}")
    suspend fun getHasilTaniUser(
        @Path("id") id:Int?
    ): HasilTaniResponse

    @FormUrlEncoded
    @POST("hasilTani/hasil_tani/hapus")
    fun deleteHasilTani(@Field("id_hasil_panen") id_hasil_panen: String?):Call<CRUDResponse>

    @GET("hasilTani/all")
    suspend fun getHasilTaniAll(
    ): HasilTaniResponse

    @Multipart
    @POST("hasilTani/hasil_tani/store")
    suspend fun tambahHasilTani(
        @Part img: MultipartBody.Part?,
        @PartMap text :Map<String, @JvmSuppressWildcards RequestBody>
    ): CRUDResponse


    @Multipart
    @POST("hasilTani/hasil_tani/edit")
    fun editHasilTani(
        @Part img: MultipartBody.Part?,
        @PartMap text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CRUDResponse>

    @GET("orderKendaraan/user/{id}/{role}")
    suspend fun listOrderByUser(
        @Path("id") id: Int?,
        @Path("role") role: String?
    ): OrderResponse

    @GET("orderKendaraan/user/{id}/{role}")
    fun listOrderByUserRe(
        @Path("id") id: Int?,
        @Path("role") role: String?
    ): Call<OrderResponse>

    @GET("kendaraan/all/{lat}/{long}")
    suspend fun allKendaraan(
        @Path("lat") lat: Double,
        @Path("long") long: Double
    ): KendaraanResponse

    @FormUrlEncoded
    @POST("orderKendaraan/user/order/petani")
    suspend fun orderKendaraan(
        @Field("alamat") alamat: String?,
        @Field("id_kendaraan") id_kendaraan: String?,
        @Field("id_petani") id_petani: String?,
        @Field("status_pembayaran") status_pembayaran: String?,
        @Field("bukti_pembayaran") bukti_pembayaran: String?,
        @Field("berat_bawaan") berat_bawaan: String?,
        @Field("harga") harga: String?,
        @Field("status") status: String?,
        @Field("lokasi_awal") lokasi_awal: String?,
        @Field("lokasi_tujuan") lokasi_tujuan: String?,
        @Field("tanggal_penjmputan") tgl_penjemputan:String,
        @Field("durasi_pengataran") durasi_pengataran: String
    ): CRUDResponse

    @FormUrlEncoded
    @POST("orderKendaraan/user/order/negotiate")
    fun negotiateOrderan(
        @Field("id") id: Int? = null,
        @Field("status") status: String? = null,
        @Field("harga") harga: String? = null,
        @Field("status_pembayaran") status_pembayaran: String? = null,
        @Field("pengaju") pengaju: String? = null,
        @Field("status_pengajuan") status_pengajuan: String? = null,
        @Field("konfirmasi_selesai") konfirmasi_selesai: String? = null,
        @Field("catatan") catatan: String? = null,
        @Field("muatan_terpenuhi") muatan_terpenuhi: String? = null,
    ): Call<CRUDResponse>


    @FormUrlEncoded
    @POST("kendaraan/updateStatusKendaraaan")
    fun updateStatusKendaraaan(
        @Field("id") id: String? = null,
        @Field("stat") stat: String?= null
    ): Call<CRUDResponse>

    @Multipart
    @POST("orderKendaraan/user/order/uploadBuktiOrder")
    fun uploadBukti(
        @Part img: MultipartBody.Part?,
        @PartMap text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CRUDResponse>

    @Multipart
    @POST("orderKendaraan/user/order/uploadBuktiSelesai")
    fun uploadBuktiSelesai(
        @Part img: MultipartBody.Part?,
        @PartMap text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CRUDResponse>

    @Multipart
    @POST("profil/update")
    fun updateProfil(
        @Part img: MultipartBody.Part?,
        @PartMap text :Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<CRUDResponse>

    @FormUrlEncoded
    @POST("trackLokasi/add")
    fun addLokasi(
        @Field("id_order") id_order: String?,
        @Field("lokasi_saat_ini") lokasi_saat_ini: String?,
        @Field("lan") lan: String?,
        @Field("lat") lat: String?,
        @Field("catatan") catatan: String?
    ): Call<CRUDResponse>

    @GET("trackLokasi/byOrder/{id}")
    fun getListLoksi(
        @Path("id") id: String
    ): Call<LokasiResponse>

    @GET("pelabuhan/all")
    fun getPelabuhan():Call<PelabuhanResponse>

    @GET("orderKendaraan/user/order/catatan/{id}")
    fun getCatatan(
        @Path("id") id:String
    ): Call<CatatanResponse>
}