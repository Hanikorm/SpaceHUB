package com.hanikorm.spacehub.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//12
object RetrofitInstance {
    private const val BASE_URL = "https://api.nasa.gov/"

    val api: NasaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NasaApiService::class.java)
    }
}
