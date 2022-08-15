package com.del.ta_10.ui.account

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.del.ta_10.databinding.ActivityEditProfilBinding
import com.del.ta_10.util.SharedPrefLogin
import com.del.ta_10.util.helper
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.HashMap

class EditProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfilBinding
    private lateinit var shaerdPref: SharedPrefLogin
    private lateinit var kategori: String
    var img1 =""
    private var Document_img1: String = ""
    private lateinit var bitmap1 : Bitmap
    var myLatitude: Double = 0.0
    var myLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        shaerdPref = SharedPrefLogin(this)
        loadDataLogin()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        if(shaerdPref.getUser()?.role=="petani"){
            binding.apply {
                edtMuatan.visibility = View.GONE
                kategori = "null"
                spinnerKategoriKendaraan.visibility = View.GONE
                edtNoRek.visibility = View.GONE
            }
        }

        binding.apply {
            btnLokasiSekarang.setOnClickListener {
                edtAlamat.setText(getLastKnownLocation(this@EditProfilActivity))
                edtLat.setText(myLatitude.toString())
                edtLong.setText(myLongitude.toString())
            }
        }

        val spineer = binding.spinnerKategoriKendaraan
        val listFilter =  resources.getStringArray(R.array.filter_driver)

        val adapter = ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, listFilter)
        spineer.adapter = adapter

        for(i in listFilter.indices){
            if(shaerdPref.getUser().jenis_kendaraan.equals(listFilter[i])){
                spineer.setSelection(i)
                break
            }
        }



        if(shaerdPref.getUser()?.potoProfil!=null){
            Glide.with(binding.photoUser)
                .load(BuildConfig.BASE_URL_FILE+shaerdPref.getUser().potoProfil)
                .apply(RequestOptions.centerInsideTransform()).placeholder(R.drawable.dummy_apple)
                .into(binding.photoUser)
        }


        spineer.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position!=0){
                    kategori = listFilter[position]
                }
//                Toast.makeText(binding.root.context, listFilter[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }

        binding.apply {

            btnPotoBaru.setOnClickListener {
                selectImage()
            }
            btnUpdate.setOnClickListener {
                var sNama = edtNama.text.toString()
                var sAlamat = edtAlamat.text.toString()
                var sMK = edtMuatan.text.toString()
                var sNP = edtNoHp.text.toString()
                var lat = edtLat.text.toString()
                var lon = edtLong.text.toString()
                var noRek = edtNoRek.text.toString()

                if(shaerdPref.getUser().role =="petani"){
                    sMK = "null"
                    noRek = "null"
                }

                updateProfil(sNama, sAlamat, kategori, sMK, sNP, lat, lon, noRek)
            }

        }


    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLastKnownLocation(context: Context): String {

        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var location: Location? = null
        for (i in providers.size - 1 downTo 0) {
            location = locationManager.getLastKnownLocation(providers[i])
            if (location != null)
                break
        }
        if (location != null) {
            myLatitude = location.latitude
            myLongitude = location.longitude
            return helper().getAddress(location.latitude, location.longitude, context)
        } else
            return "Data tidak ditemukan"

    }


    private fun loadDataLogin() {
        var user = shaerdPref.getUser()
        binding.apply {
            edtNama.setText(user.namaPetani)
            edtAlamat.setText(user.alamat)
            edtNoHp.setText(user.noTelepon)
            edtMuatan.setText(user.muatan_kendaraan)
            edtLat.setText(user.lat.toString())
            edtLong.setText(user.lon.toString())
            edtNoRek.setText(user.no_tf)
        }
    }


    private fun updateProfil(
        nama: String,
        alamat: String,
        jenis_kendaraan: String,
        muatan_kendaraan: String,
        no_telepon: String,
        lat:String,
        lon:String,
        no_rek: String
    ) {
        val map = HashMap<String, RequestBody>()
        map.put("id", createFormatString(shaerdPref.getUser().idPetani.toString()))
        map.put("nama", createFormatString(nama.toString()))
        map.put("alamat", createFormatString(alamat.toString()))
        map.put("jenis_kendaraan", createFormatString(jenis_kendaraan.toString()))
        map.put("muatan_kendaraan", createFormatString(muatan_kendaraan.toString()))
        println(no_telepon)
        map.put("no_telepon", createFormatString(no_telepon.toString()))
        map.put("lat", createFormatString(lat))
        map.put("lon", createFormatString(lon))
        map.put("no_rek", createFormatString(no_rek))
        var file1: File? = null

        var gmbr1: MultipartBody.Part? = null

        if (img1.isNotEmpty()) {
            file1 = createTempFile(bitmap1)
            val reqFile1 = file1.asRequestBody("image/*".toMediaTypeOrNull())

            gmbr1 = MultipartBody.Part.createFormData("img", file1.name, reqFile1)
        }
        RetrofitClient.apiInstance.updateProfil(gmbr1,map)
            .enqueue(object : Callback<CRUDResponse> {
                override fun onResponse(
                    call: Call<CRUDResponse>,
                    response: Response<CRUDResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditProfilActivity,
                            response?.body()?.massage,
                            Toast.LENGTH_SHORT
                        ).show()

                        val tempUser = response?.body()?.user
                        val dt = Gson().toJson(tempUser)
                        shaerdPref.resetDataUser(dt)
                    }else{
                        Toast.makeText(
                            this@EditProfilActivity,
                            response?.body()?.massage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                    Toast.makeText(
                        this@EditProfilActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
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
                val photoURI =  FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", f)
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
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
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
                    binding.photoUser.setImageBitmap(bitmap1)
                    BitMapToString(bitmap1)

                    val path = (Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator
                            + "Phoenix" + File.separator + "default")
                    f.delete()
                    var outFile: OutputStream? = null
                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
//                    binding.btnPotoBaru.text = file.name.toString().toEditable()
                    img1 =  file.name.toString()
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
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }else if (requestCode == 2) {
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
                    binding.photoUser.setImageBitmap(bitmap1)
                    img1 = imgNama.name.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun createTempFile(bitmap: Bitmap): File {
        var file  = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString()+"_image.PNG")
        var bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos)
        var bitmapData = bos.toByteArray()

        try {
            var fos =  FileOutputStream(file)
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

    @NonNull
    fun createFormatString(descriptionString:String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString);
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)



}