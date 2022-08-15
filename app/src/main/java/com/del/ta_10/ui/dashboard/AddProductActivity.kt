package com.del.ta_10.ui.dashboard

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.ta_10.BuildConfig
import com.del.ta_10.R
import com.del.ta_10.data.network.RetrofitClient
import com.del.ta_10.data.response.CRUDResponse
import com.del.ta_10.data.response.DataTani
import com.del.ta_10.databinding.ActivityAddProductBinding
import com.del.ta_10.ui.order.OrderDetailActivity
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.vo.Resource
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private var _binding: ActivityAddProductBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: DashboardViewModel by viewModel()
    var img1 = ""
    private var Document_img1: String = ""
    private lateinit var bitmap1: Bitmap
    private lateinit var user: SharedPrefLogin
    var dataIntent: DataTani? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        _binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataIntent = intent.extras?.getParcelable(DetailProductActivity.PRODUCT)

        binding.apply {

            gbrProduk.setOnClickListener {
                selectImage()
            }

            user = SharedPrefLogin(this@AddProductActivity)

            if (dataIntent != null) {
                btnTambah.setText("Ubah")
                edtNama.setText(dataIntent?.jenisHasilTani)
                edtBerat.setText(dataIntent?.beratBarang)
                edtHarga.setText(dataIntent?.harga)

                Glide.with(imgProduk)
                    .load(BuildConfig.BASE_URL_FILE + dataIntent?.img)
                    .apply(RequestOptions.centerInsideTransform())
                    .placeholder(R.drawable.dummy_apple)
                    .into(imgProduk)

                btnTambah.setOnClickListener {
                    binding.loadingBg.visibility = View.VISIBLE
                    binding.nestedProgress.visibility = View.VISIBLE
                    val nama = edtNama.text.toString()
                    var berat = edtBerat.text.toString()
                    var harga = edtHarga.text.toString()
                    saveData(nama, berat, harga)
                }
            } else {
                btnTambah.setOnClickListener {
                    binding.loadingBg.visibility = View.VISIBLE
                    binding.nestedProgress.visibility = View.VISIBLE
                    val nama = edtNama.text.toString()
                    var berat = edtBerat.text.toString()
                    var harga = edtHarga.text.toString()

                    saveData(nama, berat, harga)
                }
            }

            btnBack.setOnClickListener {
                onBackPressed()
            }
        }

    }

    private fun saveData(nama: String, berat: String, harga: String) {
        val map = HashMap<String, RequestBody>()
        map.put("id_petani", createFormatString(user.getUser().idPetani.toString()))
        map.put("berat_barang", createFormatString(berat))
        map.put("harga", createFormatString(harga))
        map.put("jenis_hasil_tani", createFormatString(nama))
        var file1: File? = null

        var gmbr1: MultipartBody.Part? = null

        if (img1.isNotEmpty()) {
            file1 = createTempFile(bitmap1)
            val reqFile1 = file1.asRequestBody("image/*".toMediaTypeOrNull())

            gmbr1 = MultipartBody.Part.createFormData("img", file1.name, reqFile1)
        }

        if (dataIntent != null) {
            map.put("id", createFormatString(dataIntent?.idHasilPanen.toString()))
            RetrofitClient.apiInstance.editHasilTani(gmbr1, map)
                .enqueue(object : Callback<CRUDResponse> {
                    override fun onResponse(
                        call: Call<CRUDResponse>,
                        response: Response<CRUDResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@AddProductActivity,
                                response.body()?.massage,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent =
                                Intent(this@AddProductActivity, DetailProductActivity::class.java)
                            dataIntent?.harga = harga
                            dataIntent?.beratBarang = berat
                            if (img1 != "") {
                                dataIntent?.img = response.body()?.content.toString()
                            }
                            dataIntent?.jenisHasilTani = nama
                            succes()
                            intent.putExtra(DetailProductActivity.PRODUCT, dataIntent)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                    }

                    override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                        Log.d("error", t.message.toString())
                    }

                })
        } else {

            productViewModel.tambahHasilTani(gmbr1, map)
                .observe(this@AddProductActivity, {
                    when (it) {
                        is Resource.Success -> {
                            Toast.makeText(
                                this@AddProductActivity,
                                it.data?.massage,
                                Toast.LENGTH_SHORT
                            ).show()
                            succes()
                            finish()
                        }

                        is Resource.Loading -> {
                            binding.loadingBg.visibility = View.VISIBLE
                            binding.nestedProgress.visibility = View.VISIBLE
                        }

                        is Resource.Error -> {
                            succes()
                        }
                    }
                })

        }


    }

    fun succes() {
        binding.loadingBg.visibility = View.GONE
        binding.nestedProgress.visibility = View.GONE
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
                    binding.imgProduk.setImageBitmap(bitmap1)
                    BitMapToString(bitmap1)

                    val path = (Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator
                            + "Phoenix" + File.separator + "default")
                    f.delete()
                    var outFile: OutputStream? = null
                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
                    binding.gbrProduk.text = file.name.toString().toEditable()
                    img1 = binding.gbrProduk.text.toString()
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
                    binding.imgProduk.setImageBitmap(bitmap1)
                    binding.gbrProduk.text = imgNama.name.toString().toEditable()
                    img1 = binding.gbrProduk.text.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun createTempFile(bitmap: Bitmap): File {
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
    }

    private fun BitMapToString(userImage1: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos)
        val b = baos.toByteArray()
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT)
        return Document_img1
    }

    @NonNull
    fun createFormatString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        );
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}