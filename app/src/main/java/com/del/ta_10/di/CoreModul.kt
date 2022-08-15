package com.del.ta_10.di

import com.del.ta_10.BuildConfig.BASE_URL
import com.del.ta_10.data.TaRepository
import com.del.ta_10.data.network.ApiService
import com.del.ta_10.data.remote.RemoteDataSource
import com.del.ta_10.domain.usecase.TaInteractor
import com.del.ta_10.domain.usecase.TaUseCase
import com.del.ta_10.ui.auth.AuthViewModel
import com.del.ta_10.ui.dashboard.DashboardViewModel
import com.del.ta_10.ui.home.HomeViewModel
import com.del.ta_10.ui.order.OrderViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()

        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module{
    single { RemoteDataSource(get()) }
    single { TaRepository(
        get()
    ) }
}

val useCaseModule = module {
    factory<TaUseCase>{ TaInteractor(get()) }
}

val viewModelModule = module{
    viewModel { AuthViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { OrderViewModel(get()) }
}