package com.del.ta_10.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.del.ta_10.domain.usecase.TaUseCase

class AuthViewModel(private val taUseCase: TaUseCase) : ViewModel() {
    fun login(username: String, password: String, role: String) =
        taUseCase.login(username, password, role).asLiveData()
}