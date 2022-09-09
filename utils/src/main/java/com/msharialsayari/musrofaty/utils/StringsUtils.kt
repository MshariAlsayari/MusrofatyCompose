package com.msharialsayari.musrofaty.utils

import java.util.*

object StringsUtils {


    fun formatArabicDigits(stringContainDigits: String?): String? {
        return stringContainDigits?.let {
            String(it.map { ch ->
                when (ch) {
                    '٠' -> '0'
                    '١' -> '1'
                    '٢' -> '2'
                    '٣' -> '3'
                    '٤' -> '4'
                    '٥' -> '5'
                    '٦' -> '6'
                    '٧' -> '7'
                    '٨' -> '8'
                    '٩' -> '9'
                    else -> ch
                }
            }.toCharArray())
        }
    }

    fun formatLongNumbers(value: Long): String {

        val suffixes: NavigableMap<Long, String> = TreeMap()
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatLongNumbers(Long.MIN_VALUE + 1)
        if (value < 0) return "-" + formatLongNumbers(-value)
        if (value < 1000) return java.lang.Long.toString(value) //deal with easy case
        val e: Map.Entry<Long, String> = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value
        val truncated = value / (divideBy / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }
}