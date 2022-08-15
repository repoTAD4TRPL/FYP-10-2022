package com.del.ta_10.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.del.ta_10.domain.usecase.TaUseCase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DashboardViewModel(private val taUseCase: TaUseCase) : ViewModel() {
    fun getProductById(id: Int) =  taUseCase.hasilTaniByUser(id).asLiveData()


    fun getProductAll() =  taUseCase.getHasilTaniAll().asLiveData()

    fun tambahHasilTani(img: MultipartBody.Part?,
                        text :Map<String, @JvmSuppressWildcards RequestBody>) =
        taUseCase.tambahHasilTani(img, text).asLiveData()
}