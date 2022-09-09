package com.msharialsayari.musrofaty.utils

import java.time.Instant
import java.time.LocalDateTime
import java.util.*

object DateUtils {

    const val DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy hh:mm:ss"
    const val DEFAULT_DATE_PATTERN = "dd/MM/yyyy"
    const val DEFAULT_MONTH_YEAR_PATTERN = "MM-yyyy"

    fun getLocalDateTimeByTimestamp(timestamp: Long): LocalDateTime? {
        return try {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}