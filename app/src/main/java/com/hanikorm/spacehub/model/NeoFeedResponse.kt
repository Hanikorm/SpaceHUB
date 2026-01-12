package com.hanikorm.spacehub.model

import com.google.gson.annotations.SerializedName

// ВОССТАНОВЛЕНО: Parcelable не нужен, так как детального экрана больше нет
data class NeoFeedResponse(
    @SerializedName("element_count")
    val elementCount: Int,
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<NeoObject>> // dates -> list
)

data class NeoObject(
    val id: String,
    val name: String,
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitudeH: Double,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproach>
)

data class CloseApproach(
    @SerializedName("close_approach_date")
    val closeApproachDate: String,
    @SerializedName("relative_velocity")
    val relativeVelocity: Map<String, String>,
    @SerializedName("miss_distance")
    val missDistance: Map<String, String>,
    @SerializedName("orbiting_body")
    val orbitingBody: String
)
