package com.hanikorm.spacehub.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Apod(
    val date: String?,
    val explanation: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    val title: String?,
    val url: String?,
    val hdurl: String?
) : Parcelable
