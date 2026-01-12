package com.hanikorm.spacehub.network

import com.hanikorm.spacehub.model.Apod
import com.hanikorm.spacehub.model.MarsPhotosResponse
import com.hanikorm.spacehub.model.NeoFeedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaApiService {

    @GET("planetary/apod")
    suspend fun getApod(@Query("api_key") apiKey: String): Response<Apod>

    @GET("planetary/apod")
    suspend fun getApodRange(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<List<Apod>>

    @GET("neo/rest/v1/feed")
    suspend fun getNeoFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): Response<NeoFeedResponse>

    @GET("mars-photos/api/v1/rovers/{rover}/photos")
    suspend fun getMarsRoverPhotos(
        @Path("rover") rover: String,
        @Query("earth_date") earthDate: String,
        @Query("api_key") apiKey: String
    ): Response<MarsPhotosResponse>
}
