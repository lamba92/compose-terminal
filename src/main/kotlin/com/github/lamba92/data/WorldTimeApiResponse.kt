package com.github.lamba92.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorldTimeApiResponse(
    @SerialName("abbreviation")
    val abbreviation: String,

    @SerialName("client_ip")
    val clientIp: String,

    @SerialName("datetime")
    val dateTime: String,

    @SerialName("day_of_week")
    val dayOfWeek: Int,

    @SerialName("day_of_year")
    val dayOfYear: Int,

    @SerialName("dst")
    val dst: Boolean,

    @SerialName("dst_from")
    val dstFrom: String,

    @SerialName("dst_offset")
    val dstOffset: Int,

    @SerialName("dst_until")
    val dstUntil: String,

    @SerialName("raw_offset")
    val rawOffset: Int,

    @SerialName("timezone")
    val timezone: String,

    @SerialName("unixtime")
    val unixTime: Long,

    @SerialName("utc_datetime")
    val utcDateTime: String,

    @SerialName("utc_offset")
    val utcOffset: String,

    @SerialName("week_number")
    val weekNumber: Int
)