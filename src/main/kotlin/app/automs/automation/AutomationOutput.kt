package app.automs.automation

import app.automs.sdk.domain.http.ResponseOutput

data class AutomationOutput(
    val temperature: String,
    val date: String,
    val sky: String,
    val wind: String,
    val humidity: String,
    val coordinates: String
) : ResponseOutput

data class GeoHashResponse(
    val boundingBox: BoundingBox,
    val inputGeohash: String,
    val lat: Double,
    val lon: Double
)

data class BoundingBox(
    val LatMax: Double,
    val LatMin: Double,
    val LonMax: Double,
    val LonMin: Double
)
