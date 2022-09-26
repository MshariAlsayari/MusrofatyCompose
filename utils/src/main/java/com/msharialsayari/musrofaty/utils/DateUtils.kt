package com.msharialsayari.musrofaty.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy hh:mm:ss"
    const val DEFAULT_DATE_PATTERN = "dd/MM/yyyy"
    const val DEFAULT_MONTH_YEAR_PATTERN = "MM-yyyy"

    @JvmStatic
    fun getDateByTimestamp(date: Long, pattern: String = DEFAULT_DATE_TIME_PATTERN): String? {
        return try {
            val formatter: DateFormat =
                SimpleDateFormat(pattern, Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            formatter.format(calendar.time)
        } catch (e: Exception) {
            null
        }
    }
}