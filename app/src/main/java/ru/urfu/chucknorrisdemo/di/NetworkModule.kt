package ru.urfu.chucknorrisdemo.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import retrofit2.converter.kotlinx.serialization.asConverterFactory
private const val BASE_URL = "https://api.chucknorris.io/"

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(get()))
            .build()
    }

    single {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .client(get())
            .build()
    }
}