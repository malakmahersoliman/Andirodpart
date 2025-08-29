package com.example.milkchequedemo.data.service

import android.util.Log
import com.example.milkchequedemo.data.service.StoreApi
import com.example.milkchequedemo.data.service.StoreApiProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StoreApiProvider {
    private const val BASE_URL =
        "https://milkchequewebapp-d4fuf2edfbc5g5hq.spaincentral-01.azurewebsites.net/"

    private val logging = HttpLoggingInterceptor { msg ->
        Log.d("HTTP", msg)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: StoreApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // note trailing slash
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(StoreApi::class.java)
    }
}
