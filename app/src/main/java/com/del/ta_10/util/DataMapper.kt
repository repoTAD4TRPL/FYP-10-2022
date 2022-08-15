package com.del.ta_10.util

import com.del.ta_10.data.response.*
import com.del.ta_10.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapResponsesAuthToDomain(input: AuthResponse): Flow<Auth> {
        return  flowOf(
            Auth(
                data = input.data,
                status = input.status,
                massage = input.massage
            )
        )
    }

    fun mapResponseHasilTani(input: HasilTaniResponse): Flow<HasilTani>{
        return flowOf(
            HasilTani(
                data = input.data,
                status = input.status,
                massage = input.massage
            )
        )
    }

    fun mapRespobseCRUD(input: CRUDResponse): Flow<CRUD>{
        return flowOf(
            CRUD(
                massage = input.massage,
                status = input.status

            )
        )
    }

    fun mapResponseOrder(input:OrderResponse): Flow<Order>{
        return flowOf(
            Order(
                massage = input.massage,
                data = input.data
            )
        )
    }

    fun mapResponseKendaraan(input: KendaraanResponse): Flow<Kendaraan>{
        return flowOf(
            Kendaraan(
                massage = input.massage,
                data = input.data
            )
        )
    }
}