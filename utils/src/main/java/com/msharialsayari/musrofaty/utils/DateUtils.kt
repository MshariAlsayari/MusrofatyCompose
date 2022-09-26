package com.msharialsayari.musrofaty.utils

import android.content.Context
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


    @JvmStatic
    fun getCurrentDate(): Long {
        val calendar = Calendar.getInstance();
        return calendar.timeInMillis
    }

    @JvmStatic
    fun getHowLongPostDate(context: Context, smsDate: Long): String {
        val diff: Long = getCurrentDate() - smsDate
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        var msg = ""
        when {
            seconds in 1..59 -> {
                msg = if (isEqualOne(seconds)) {
                    getLocalizedString(
                        context, getNumberType(
                            seconds
                        ), DateTimeUnit.SECONDS
                    )
                } else {
                    "$seconds " + getLocalizedString(
                        context, getNumberType(
                            seconds
                        ), DateTimeUnit.SECONDS
                    )
                }
            }
            minutes in 1..59 -> {
                msg = if (isEqualOne(minutes)) {
                    getLocalizedString(
                        context, getNumberType(
                            minutes
                        ), DateTimeUnit.MINUTES
                    )

                } else {
                    "$minutes " + getLocalizedString(
                        context, getNumberType(
                            minutes
                        ), DateTimeUnit.MINUTES
                    )

                }

            }
            hours in 1..24 -> {
                msg = if (isEqualOne(hours)) {
                    getLocalizedString(
                        context, getNumberType(
                            hours
                        ), DateTimeUnit.HOURS
                    )

                } else {
                    "$hours " + getLocalizedString(
                        context, getNumberType(
                            hours
                        ), DateTimeUnit.HOURS
                    )

                }

            }
            days == 1L -> {
                msg = getLocalizedString(
                    context,
                    getNumberType(minutes),
                    DateTimeUnit.DAYS
                )
            }

        }



        return if (msg.isEmpty()) {
            getDateByTimestamp(smsDate)?:""
        } else {
            if (Locale.getDefault().language == "ar")
                context.getString(R.string.common_ago) + " " + msg
            else
                msg + " " + context.getString(R.string.common_ago)

        }


    }


    private fun getLocalizedString(
        context: Context,
        numberType: NumberType,
        dateTimeUnit: DateTimeUnit
    ): String {
        var unit = ""
        when (dateTimeUnit) {
            DateTimeUnit.SECONDS -> {
                unit = when (numberType) {
                    NumberType.SINGLE -> context.getString(R.string.common_second)
                    NumberType.TWICE -> context.getString(R.string.common_two_seconds)
                    NumberType.PLURAL -> context.getString(R.string.common_seconds)
                }
            }
            DateTimeUnit.MINUTES -> {
                unit = when (numberType) {
                    NumberType.SINGLE -> context.getString(R.string.common_minute)
                    NumberType.TWICE -> context.getString(R.string.common_two_minutes)
                    NumberType.PLURAL -> context.getString(R.string.common_minutes)
                }
            }
            DateTimeUnit.HOURS -> {
                unit = when (numberType) {
                    NumberType.SINGLE -> context.getString(R.string.common_hour)
                    NumberType.TWICE -> context.getString(R.string.common_two_hours)
                    NumberType.PLURAL -> context.getString(R.string.common_hours)
                }
            }
            DateTimeUnit.DAYS -> {
                unit = when (numberType) {
                    NumberType.SINGLE -> context.getString(R.string.common_day)
                    NumberType.TWICE -> context.getString(R.string.common_two_days)
                    NumberType.PLURAL -> context.getString(R.string.common_days)
                }
            }
        }

        return unit
    }


    private fun getNumberType(number: Long): NumberType {
        return when {
            number == 1L || number >= 11 -> {
                NumberType.SINGLE

            }
            number == 2L -> {
                NumberType.TWICE
            }
            else -> {
                NumberType.PLURAL
            }
        }
    }

    private fun isEqualOne(number: Long): Boolean {
        return number == 1L

    }



    enum class NumberType {
        SINGLE, TWICE, PLURAL
    }

    enum class DateTimeUnit {
        SECONDS, MINUTES, HOURS, DAYS
    }
}