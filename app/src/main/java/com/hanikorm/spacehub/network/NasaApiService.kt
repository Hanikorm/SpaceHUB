package com.hanikorm.spacehub.network

import com.hanikorm.spacehub.model.Apod
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getApodList(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int = 10
    ): List<Apod>
}