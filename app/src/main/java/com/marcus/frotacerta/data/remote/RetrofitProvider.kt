package com.marcus.frotacerta.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL =
        "https://jsonplaceholder.typicode.com/"

    val api: FrotaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(FrotaApiService::class.java)
    }
}
