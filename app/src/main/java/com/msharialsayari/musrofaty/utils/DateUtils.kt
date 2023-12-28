package com.msharialsayari.musrofaty.utils

import android.content.Context
import com.google.type.DateTime
import com.msharialsayari.musrofaty.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
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
    fun getToday(): LocalDateTime {
        return LocalDateTime.now()
    }

    fun getDisplayMonth(
        date: LocalDateTime,
        textStyle: TextStyle = TextStyle.FULL,
        locale: Locale = Locale.getDefault()
    ): String {
        return date.month.getDisplayName(textStyle, locale)
    }

    @JvmStatic
    fun parseDate(value:String , pattern: String = DEFAULT_DATE_PATTERN): Long {
        val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
        return formatter.parse(value)?.time ?:0
    }

    @JvmStatic
    fun formatDate(date: String, pattern: String = DEFAULT_DATE_PATTERN): String {
        val formatter: DateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
        return formatter.format(formatter.parse(date)!!)
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
            getDateByTimestamp(smsDate) ?:""
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

    fun getSalaryDate():Long{
        val currentCalender = Calendar.getInstance()
        val salaryDay = 27
        val day = currentCalender.get(Calendar.DAY_OF_MONTH)
        if (day < salaryDay ) {
            currentCalender.add(Calendar.MONTH, -1)
        }

        val year = currentCalender.get(Calendar.YEAR)
        val month = currentCalender.get(Calendar.MONTH)
        currentCalender.set(year,month,salaryDay)
        return currentCalender.timeInMillis
    }


    fun toTimestamp(date:LocalDate):Long{
        val calendar = Calendar.getInstance()
        calendar.set(date.year,date.monthValue-1,date.dayOfMonth)
        return calendar.timeInMillis
    }

    fun formattedRangeDate(start:Long, end:Long, pattern: String = DEFAULT_DATE_PATTERN):String{
        val formattedStartDate = getDateByTimestamp(start,pattern) ?:""
        val formattedEndDate   = getDateByTimestamp(end,pattern) ?:""
        return "$formattedStartDate - $formattedEndDate"

    }



    enum class NumberType {
        SINGLE, TWICE, PLURAL
    }

    enum class DateTimeUnit {
        SECONDS, MINUTES, HOURS, DAYS
    }

    enum class FilterOption(val id :Int, val title:Int,val subtitle:Int= R.string.empty){
        ALL(0, R.string.filter_options_all_title),
        TODAY(1, R.string.filter_options_today_title),
        WEEK(2, R.string.filter_options_week_title, R.string.filter_options_week_subtitle),
        MONTH(3, R.string.filter_options_month_title),
        YEAR(4, R.string.filter_options_year_title),
        RANGE(5, R.string.filter_options_range_title);


        companion object{
            fun getFilterOption(id:Int? = 0): FilterOption {
                return when(id){
                    1-> TODAY
                    2-> WEEK
                    3-> MONTH
                    4-> YEAR
                    5-> RANGE
                    else -> ALL
                }

            }


            fun isRangeDateSelected(selectedId:Int?):Boolean = selectedId == 5
        }



    }
}