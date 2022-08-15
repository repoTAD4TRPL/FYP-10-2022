package com.del.ta_10.data

import com.del.ta_10.data.response.AuthResponse
import com.del.ta_10.data.response.HasilTaniResponse
import com.del.ta_10.vo.Resource
import kotlinx.coroutines.flow.Flow

interface TaDataSource {
    fun login(username: String, password: String, role: String): Flow<Resource<AuthResponse>>

    fun hasilTaniByUser(id: Int): Flow<Resource<HasilTaniResponse>>
}