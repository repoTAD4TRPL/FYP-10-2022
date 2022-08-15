package com.del.ta_10.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.del.ta_10.domain.usecase.TaUseCase

class HomeViewModel(private val taUseCase: TaUseCase) : ViewModel() {

    fun getDummyOrder(id: Int, role: String) = taUseCase.listOrderByUser(id, role).asLiveData()

}