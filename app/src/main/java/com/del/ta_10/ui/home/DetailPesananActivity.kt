package com.del.ta_10.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.del.ta_10.R
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.data.response.DataOrder
import com.del.ta_10.databinding.ActivityDetailPesananBinding
import com.del.ta_10.util.SharedPrefLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.util.helper
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.util.HashMap


class DetailPesananActivity : AppCompatActivity() {
    private var _binding: ActivityDetailPesananBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefLogin: SharedPrefLogin
    private var pesanan: DataOrder? = null
    var img1 = ""
    private var Document_img1: String = ""
    private lateinit var bitmap1: Bitmap
    private var bukti_tf = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefLogin = SharedPrefLogin(this)
        supportActionBar?.hide()

        pesanan = intent.extras?.getParcelable(PESAAN)
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnCatatanLain.setOnClickListener {
                daftarCatatan(this@DetailPesananActivity, pesanan?.idOrder.toString())
            }

            btnTrackLokasi.setOnClickListener {
                sharedPrefLogin.getUser().role?.let { it1 ->
                    DialogLocation().showDialogDetailTracking(
                        this@DetailPesananActivity, pesanan?.idOrder.toString(),
                        it1
                    )
                }
            }
            if (pesanan?.status == "negosiasi") {
                if (pesanan?.pengaju == "supir") {
                    txtCatatan.text = "Catatan Supir : ${pesanan?.catatan}"
                } else {
                    txtCatatan.text = "Catatan Petani : ${pesanan?.catatan}"
                }
            } else {
                if (pesanan?.catatan != null) {
                    if (pesanan?.pengaju == "supir") {
                        txtCatatan.text = "Catatan Supir : ${pesanan?.catatan} (Selesai)"
                    } else {
                        txtCatatan.text = "Catatan Petani : ${pesanan?.catatan} (Selesai)"
                    }
                } else {
                    txtCatatan.text = "Tidak ada catatan"
                }
            }
            if (sharedPrefLogin.getUser().role == "petani") {
                textTransfer.text = pesanan?.no_tf
                btnTolak.visibility = View.GONE
                textForUser.text = "Supir : "
                textUser.text = pesanan?.namaSupir
                btnTerima.visibility = View.GONE
                gbrBuktiSelesai.visibility = View.GONE
                btnTrackLokasi.setText("Lihat Riwayat Lokasi")
                menuPetani()

                if (pesanan?.statusPembayaran.equals("Tolak Bukti")) {
                    gbrBukti.visibility = View.VISIBLE
                    textStatusBuktiSelesai.text =
                        "Bukti pembayaran anda ditolak, sialhkan upload ulang"
                }

                if (pesanan?.status_pengajuan.equals("proses")) {
                    if (pesanan?.pengaju.equals("petani")) {
                        binding.btnTerima.visibility = View.GONE
                    } else {
                        binding.btnTerima.visibility = View.VISIBLE
                    }
                }

                if (pesanan?.status.equals("Konfrimasi Bukti Selesai")) {
                    btnTerimaBuktiSelesai.visibility = View.VISIBLE
                    btnTolakBuktiSelesai.visibility = View.VISIBLE
                }
            } else if (sharedPrefLogin.getUser().role == "supir") {

                textTransfer.text = sharedPrefLogin.getUser()?.no_tf
                btnTrackLokasi.setText("Tambah Riwayat Lokasi")
                btnBatalkan.visibility = View.GONE
                textForUser.text = "Petani : "
                textUser.text = pesanan?.namaPetani
                gbrBukti.visibility = View.GONE
                menuPengemudi()

                if (pesanan?.status_pengajuan.equals("proses")) {
                    if (pesanan?.pengaju.equals("supir")) {
                        binding.btnTerima.visibility = View.GONE
                    } else {
                        binding.btnTerima.visibility = View.VISIBLE
                    }
                }

                if (pesanan?.konfirmasi_selesai.equals("Tolak")) {
                    gbrBuktiSelesai.visibility = View.VISIBLE
                    textStatusBukti.text = "Bukti selesai anda ditolak, sialhkan upload ulang"
                }
            }

            if (pesanan?.status_pengajuan.equals("selesai")) {
                setDisableSelesaiPengajuan()
            }

            if (pesanan?.status.equals("Selesai")) {
                markButtonDisable(btnTolak)
                markButtonDisable(btnTerima)
                markButtonDisable(btnBatalkan)
                gbrBuktiSelesai.visibility = View.GONE
            }

            gbrBukti.setOnClickListener {
                bukti_tf = true
                selectImage()
            }

            gbrBuktiSelesai.setOnClickListener {
                bukti_tf = false
                selectImage()
            }



            textIdPesanan.text = pesanan?.idOrder.toString()
            textBeratBawaan.text = pesanan?.beratBawaan.toString()
            textHarga.text = helper().convertRupiah(pesanan?.harga)
            textJenisKendaraan.text = pesanan?.jenisKendaraan
            textKapasitasKendaran.text = pesanan?.muatanKendaraan
            textLokasiAkhir.text = pesanan?.lokasiTujuan
            textOrderLokasiAwal.text = pesanan?.lokasiAwal
            textStatusBukti.text = pesanan?.statusPembayaran
            pesanan?.status?.let { setStatusPesanan(it) }
            if (pesanan?.buktiPembayaran != "Tidak ada" && pesanan?.buktiPembayaran != "proses" && pesanan?.buktiPembayaran != null) {
                Glide.with(imgBukti)
                    .load(BuildConfig.BASE_URL_FILE + pesanan?.buktiPembayaran)
                    .apply(RequestOptions.centerInsideTransform())
                    .placeholder(R.drawable.ic_baseline_image)
                    .into(imgBukti)

                zoomImageBukti()
            }

            if (pesanan?.statusPembayaran != "Terima Bukti" && (pesanan?.status == "Selesai" || pesanan?.status != null || pesanan?.status == "negosiasi")) {
                binding.gbrBuktiSelesai.visibility = View.GONE
            }

            if (pesanan?.bukti_selesai != null && pesanan?.bukti_selesai != "Tidak ada" && pesanan?.bukti_selesai != "proses") {
                Glide.with(imgBuktiSelesai)
                    .load(BuildConfig.BASE_URL_FILE + pesanan?.bukti_selesai)
                    .apply(RequestOptions.centerInsideTransform())
                    .placeholder(R.drawable.ic_baseline_image)
                    .into(imgBuktiSelesai)
                zoomImageSelesai()

            }
            if (pesanan?.konfirmasi_selesai == "Tolak") {
                binding.textStatusBuktiSelesai.text = "Bukti Di Tolak"
            } else {
                if (pesanan?.status?.equals("Konfrimasi Bukti Selesai") == true) {
                    binding.textStatusBuktiSelesai.text = "Menunggu Konfirmasi"
                } else if (pesanan?.status?.equals("Selesai") == true) {
                    binding.textStatusBuktiSelesai.text = "Selesai"
                }
            }

            btnPengajuan.setOnClickListener {
                showNegotiatePopUp(pesanan?.idOrder)
            }

            btnTerima.setOnClickListener {
                prosesOrderan(
                    pesanan?.idOrder,
                    "Di terima ${sharedPrefLogin.getUser()?.role}",
                    "selesai"
                )
            }

            btnTolak.setOnClickListener {
                prosesOrderan(pesanan?.idOrder, "tolak")
            }

            btnBatalkan.setOnClickListener {
                prosesOrderan(pesanan?.idOrder, "batalkan")
            }

            btnTolakBukti.setOnClickListener {
                prosesBuktiOrderan(pesanan?.idOrder, "Tolak Bukti", null, null)
            }

            btnTerimaBukti.setOnClickListener {
                prosesBuktiOrderan(pesanan?.idOrder, "Terima Bukti", null, null)
            }

            btnUp.setOnClickListener {
                saveData(pesanan?.idOrder)
            }

            btnKonfirmasiSelesai.setOnClickListener {
                prosesOrderan(pesanan?.idOrder, "Konfrimasi Bukti Selesai")
            }

            btnTerimaBuktiSelesai.setOnClickListener {
                prosesBuktiOrderan(pesanan?.idOrder, null, "Selesai", "Selesai")
            }

            btnTolakBuktiSelesai.setOnClickListener {
                prosesBuktiOrderan(pesanan?.idOrder, null, "Tolak", null)
            }
        }

    }

    private fun setDisableSelesaiPengajuan() {
        binding.apply {
            markButtonDisable(btnPengajuan)
            markButtonDisable(btnTolak)
            markButtonDisable(btnTerima)
        }
    }

    private fun showNegotiatePopUp(id: Int?) {
        BottomSheetDialog(this).apply {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
            setCancelable(true)
            setContentView(R.layout.layout_negotiate)
            val newPrice = this.findViewById<EditText>(R.id.edt_harag_negosiasi)?.text
            val catatan = this.findViewById<EditText>(R.id.edt_harag_catatan)?.text

            val btn = this.findViewById<Button>(R.id.btn_ajukan)
            btn?.setOnClickListener {
                RetrofitClient.apiInstance.negotiateOrderan(
                    id = id,
                    status = "negosiasi",
                    harga = newPrice.toString(),
                    pengaju = sharedPrefLogin.getUser().role,
                    status_pengajuan = "proses",
                    catatan = catatan.toString()
                )
                    .enqueue(object : Callback<CRUDResponse> {
                        override fun onResponse(
                            call: Call<CRUDResponse>,
                            response: Response<CRUDResponse>
                        ) {
                            Toast.makeText(
                                this@DetailPesananActivity,
                                response.body()?.massage,
                                Toast.LENGTH_SHORT
                            ).show()
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@DetailPesananActivity,
                                    response.body()?.massage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.apply {
                                    textHarga.text = helper().convertRupiah(newPrice.toString().toInt())
                                    btnTerima.visibility = View.GONE
                                    if (pesanan?.pengaju == "supir") {
                                        txtCatatan.text = "Catatan Supir : ${catatan}"
                                    } else {
                                        txtCatatan.text = "Catatan Petani : ${catatan}"
                                    }
                                }

                            } else Log.d("Error : ", response.errorBody().toString())
                        }

                        override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                            Log.d("Error : ", t.message.toString())
                        }

                    })
                dismiss()
            }

        }.show()
    }


    private fun prosesBuktiOrderan(
        id: Int?,
        statusPembayaran: String?,
        konfirmasiSelesai: String?,
        status: String?
    ) {
        AlertDialog.Builder(binding.root.context).apply {
            var msg = ""
            if (konfirmasiSelesai != null) {
                msg = konfirmasiSelesai
            } else if (statusPembayaran != null) {
                msg = statusPembayaran
            }
            setMessage("Apakah anda yakin $msg bukti ini?")
            setPositiveButton(
                "Iya"
            ) { dialog, _ ->
                RetrofitClient.apiInstance.negotiateOrderan(
                    id = id,
                    status_pembayaran = statusPembayaran,
                    konfirmasi_selesai = konfirmasiSelesai,
                    status = status
                )
                    .enqueue(object : Callback<CRUDResponse> {
                        override fun onResponse(
                            call: Call<CRUDResponse>,
                            response: Response<CRUDResponse>
                        ) {
                            Toast.makeText(
                                this@DetailPesananActivity,
                                response.body()?.massage,
                                Toast.LENGTH_SHORT
                            ).show()
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@DetailPesananActivity,
                                    response.body()?.massage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (status == "Terima Bukti") {
                                    binding.apply {
                                        btnTerima.visibility = View.GONE
                                        btnKonfirmasiSelesai.visibility = View.VISIBLE
                                    }
                                }
                                if (statusPembayaran == "Terima Bukti") {
                                    binding.apply {
                                        btnKonfirmasiSelesai.visibility = View.VISIBLE
                                        gbrBuktiSelesai.visibility = View.VISIBLE
                                        btnTerimaBukti.visibility = View.GONE
                                        btnTolakBukti.visibility = View.GONE
                                    }
                                }
                                if (konfirmasiSelesai == "Selesai" && status == "Selesai") {
                                    binding.apply {
                                        btnTerimaBuktiSelesai.visibility = View.GONE
                                        btnTolakBuktiSelesai.visibility = View.GONE
                                    }
                                }
                                if (konfirmasiSelesai != null && status != null) {
                                    setStatusPesanan(status)
                                } else if (status != null) {
                                    binding.textStatusBukti.text = statusPembayaran
                                }
                            } else Log.d("Error : ", response.errorBody().toString())
                        }

                        override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                            Log.d("Error : ", t.message.toString())
                        }

                    })
                dialog.cancel()
            }
            setNegativeButton(
                "Tidak"
            ) { dialog, _ -> dialog.cancel() }
            create()
        }.show()
    }

    private fun prosesOrderan(id: Int?, status: String, statusPengajuan: String? = null) {
        AlertDialog.Builder(binding.root.context).apply {
            setMessage("Apakah anda yakin $status ini?")
            setPositiveButton(
                "Iya"
            ) { dialog, _ ->
                RetrofitClient.apiInstance.negotiateOrderan(
                    id = id,
                    status = status,
                    status_pengajuan = statusPengajuan,
                    muatan_terpenuhi = pesanan?.beratBawaan.toString()
                )
                    .enqueue(object : Callback<CRUDResponse> {
                        override fun onResponse(
                            call: Call<CRUDResponse>,
                            response: Response<CRUDResponse>
                        ) {
                            Toast.makeText(
                                this@DetailPesananActivity,
                                response.body()?.massage,
                                Toast.LENGTH_SHORT
                            ).show()
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@DetailPesananActivity,
                                    response.body()?.massage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                setStatusPesanan(status)
                                sharedPrefLogin.getUser()?.muatan_terpenuhi =
                                    sharedPrefLogin.getUser().muatan_terpenuhi + pesanan?.beratBawaan?.toInt()
                                setDisableSelesaiPengajuan()
                            } else Log.d("Error : ", response.errorBody().toString())
                        }

                        override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                            Log.d("Error : ", t.message.toString())
                        }

                    })
                dialog.cancel()
            }
            setNegativeButton(
                "Tidak"
            ) { dialog, _ -> dialog.cancel() }
            create()
        }.show()


    }

    private fun menuPetani() {
        binding.gbrBukti.visibility = View.GONE
        if ((pesanan?.status == "Di terima supir" || pesanan?.status == "Di terima petani") && pesanan?.statusPembayaran == "Proses") {
            binding.gbrBukti.visibility = View.VISIBLE
        } else if (pesanan?.status == "batalkan") {
            markButtonDisable(binding.btnPengajuan)
            markButtonDisable(binding.btnBatalkan)
        }
        if (pesanan?.statusPembayaran == "Terima Bukti") {
            binding.btnCallDriver.visibility = View.VISIBLE
        }
        binding.btnCallDriver.setOnClickListener {
            var url = "https://api.whatsapp.com/send?phone=${pesanan?.telepon}"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    fun setStatusPesanan(status: String) {
        val context = binding.root.context
        var stats = ""
        binding.apply {

            when (status) {
                "batalkan" -> {
                    stats = "Batal"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_batal
                        )
                    )
                }
                "Selesai" -> {
                    stats = "Selesai"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_selesai
                        )
                    )
                }
                "Di terima supir" -> {
                    stats = "Di terima supir"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_proses
                        )
                    )
                }
                "Di terima petani" -> {
                    stats = "Di terima petani"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_proses
                        )
                    )
                }
                "negosiasi" -> {
                    stats = "DiTawar"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_proses
                        )
                    )
                }
                "Konfrimasi Bukti Selesai" -> {
                    stats = "Konfirmasi Bukti"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_proses
                        )
                    )
                }
                "Proses" -> {
                    stats = "Proses"
                    statusPesanan.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_proses
                        )
                    )
                }
            }
            statusPesanan.text = stats
        }
    }

    private fun menuPengemudi() {
        binding.apply {
            if (pesanan?.status == "batalkan") {
                markButtonDisable(btnTolak)
                markButtonDisable(btnPengajuan)
                markButtonDisable(btnTerima)
            }
            gbrBukti.visibility = View.GONE
            if (pesanan?.statusPembayaran == "Proses Persetujuan" || pesanan?.statusPembayaran == "Tolak Bukti") {
                btnPengajuan.visibility = View.GONE
                btnBatalkan.visibility = View.GONE
                btnTolak.visibility = View.GONE
                btnTerimaBukti.visibility = View.VISIBLE
                btnTolakBukti.visibility = View.VISIBLE
                btnTerima.visibility = View.GONE
                gbrBuktiSelesai.visibility = View.GONE
            }

            if (pesanan?.statusPembayaran == "Terima Bukti") {
                btnTerima.visibility = View.GONE
                btnKonfirmasiSelesai.visibility = View.VISIBLE
            }
        }
    }

    private fun markButtonDisable(button: Button) {
        button.isEnabled = false
        button.setTextColor(resources.getColor(R.color.white))
        button.setBackgroundColor(resources.getColor(R.color.md_grey_700))
    }

    fun saveData(id: Int?) {
        val map = HashMap<String, RequestBody>()
        map.put("id", createFormatString(id.toString()))
        map.put("bukti_pembayaran", createFormatString("Proses Persetujuan"))
        map.put("konfirmasi_selesai", createFormatString("Menunggu Persetujuan"))

        var file1: File?

        var gmbr1: MultipartBody.Part? = null

        if (img1.isNotEmpty()) {
            file1 = createTempFile(bitmap1)
            val reqFile1 = file1.asRequestBody("image/*".toMediaTypeOrNull())

            gmbr1 = MultipartBody.Part.createFormData("img", file1.name, reqFile1)
        }

        if (bukti_tf) {
            RetrofitClient.apiInstance.uploadBukti(gmbr1, map)
                .enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>
                    ) {
                        Toast.makeText(
                            this@DetailPesananActivity,
                            response.body()?.massage,
                            Toast.LENGTH_SHORT
                        ).show()
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@DetailPesananActivity,
                                response.body()?.massage,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else Log.d("Error : ", response.errorBody().toString())
                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                        Log.d("Error : ", t.message.toString())
                    }

                })
        } else {
            RetrofitClient.apiInstance.uploadBuktiSelesai(gmbr1, map)
                .enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>
                    ) {
                        Toast.makeText(
                            this@DetailPesananActivity,
                            response.body()?.massage,
                            Toast.LENGTH_SHORT
                        ).show()
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@DetailPesananActivity,
                                response.body()?.massage,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else Log.d("Error : ", response.errorBody().toString())
                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                        Log.d("Error : ", t.message.toString())
                    }

                })
        }


    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val photoURI = FileProvider.getUriForFile(
                    this,
                    this.getApplicationContext().getPackageName() + ".provider",
                    f
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, 1)
            } else if (options[item] == "Choose from Gallery") {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                var f = File(Environment.getExternalStorageDirectory().toString())
                for (temp in f.listFiles()) {
                    if (temp.name == "temp.jpg") {
                        f = temp
                        break
                    }
                }
                try {
                    val bitmapOptions = BitmapFactory.Options()
                    bitmap1 = BitmapFactory.decodeFile(f.absolutePath, bitmapOptions)
                    if (bukti_tf) {
                        binding.imgBukti.setImageBitmap(bitmap1)
                    } else {
                        binding.imgBuktiSelesai.setImageBitmap(bitmap1)
                    }
                    BitMapToString(bitmap1)

                    val path = (Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator
                            + "Phoenix" + File.separator + "default")
                    f.delete()
                    var outFile: OutputStream? = null
                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
                    if (bukti_tf) {
                        binding.gbrBukti.text = file.name.toString().toEditable()
                        img1 = binding.gbrBukti.text.toString()
                    } else {
                        binding.gbrBuktiSelesai.text = file.name.toString().toEditable()
                        img1 = binding.gbrBuktiSelesai.text.toString()
                    }
                    try {
                        outFile = FileOutputStream(file)
                        bitmap1.compress(Bitmap.CompressFormat.JPEG, 85, outFile)
                        outFile.flush()
                        outFile.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == 2) {
                val selectedImage = data?.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? = contentResolver.query(
                    selectedImage!!,
                    filePathColumn, null, null, null
                )
                cursor?.moveToFirst()
                val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
                val picturePath: String = cursor.getString(columnIndex)
                val imgNama = File(picturePath)
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    if (bukti_tf) {
                        binding.imgBukti.setImageBitmap(bitmap1)
                        binding.gbrBukti.text = imgNama.name.toString().toEditable()
                        img1 = binding.gbrBukti.text.toString()
                    } else {
                        binding.imgBuktiSelesai.setImageBitmap(bitmap1)
                        binding.gbrBuktiSelesai.text = imgNama.name.toString().toEditable()
                        img1 = binding.gbrBuktiSelesai.text.toString()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            binding.btnUp.visibility = View.VISIBLE
        }
    }

    fun createTempFile(bitmap: Bitmap): File {
        var file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_image.PNG"
        )
        var bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        var bitmapData = bos.toByteArray()

        try {
            var fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file;

        println(file.toString())
    }

    private fun BitMapToString(userImage1: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos)
        val b = baos.toByteArray()
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT)
        return Document_img1
    }

    private fun zoomImageBukti() {
        binding.apply {
            val urlBukti = GlideUrl(
                BuildConfig.BASE_URL_FILE + pesanan?.buktiPembayaran, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )

            imgBukti.setOnClickListener {
                Dialog(binding.root.context).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(true)
                    setContentView(R.layout.layout_detail_poto)
                    val img = this.findViewById<ImageView>(R.id.img_detail_photo)

                    Glide.with(binding.root.context).load(urlBukti)
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img)

                }.show()
            }
        }
    }

    private fun zoomImageSelesai() {
        binding.apply {
            val urlSelesai = GlideUrl(
                BuildConfig.BASE_URL_FILE + pesanan?.bukti_selesai, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )

            imgBuktiSelesai.setOnClickListener {
                Dialog(binding.root.context).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(true)
                    setContentView(R.layout.layout_detail_poto)
                    val img = this.findViewById<ImageView>(R.id.img_detail_photo)

                    Glide.with(binding.root.context).load(urlSelesai)
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img)

                }.show()
            }
        }
    }

    @NonNull
    fun createFormatString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        );
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun daftarCatatan(context: Context, id: String){
        DialogCatatan().getListCatatan(context, id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        pesanan = null
    }

    companion object {
        const val PESAAN = "pesanan"
    }


}