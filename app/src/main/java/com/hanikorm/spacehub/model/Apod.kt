package com.hanikorm.spacehub.model

import com.squareup.moshi.Json

data class Apod(
    val title: String,
    val date: String,
    val explanation: String,
    @Json(name = "url") val imageUrl: String?,
    @Json(name = "media_type") val mediaType: String
)