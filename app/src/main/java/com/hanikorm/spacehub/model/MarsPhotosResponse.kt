package com.hanikorm.spacehub.model

import com.google.gson.annotations.SerializedName

data class MarsPhotosResponse(
    val photos: List<MarsPhoto>
)

data class MarsPhoto(
    val id: Int,
    val sol: Int,
    val camera: Camera,
    @SerializedName("img_src")
    val imgSrc: String,
    @SerializedName("earth_date")
    val earthDate: String,
    val rover: Rover
)

data class Camera(val id: Int, val name: String, @SerializedName("rover_id") val roverId: Int, @SerializedName("full_name") val fullName: String)
data class Rover(val id: Int, val name: String, @SerializedName("landing_date") val landingDate: String, val status: String)
