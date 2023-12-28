package com.msharialsayari.musrofaty.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.NavigableMap
import java.util.TreeMap
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object StringsUtils {


    @JvmStatic
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

    fun formatNumberWithComma(number:String):String{
        val amount: Double = number.toDouble()
        val formatter = DecimalFormat("#,###.00", DecimalFormatSymbols(Locale.ENGLISH))
        return try {
            val formattedAmount =  formatter.format(amount)
            if (formattedAmount == ".00"  ||formattedAmount == "00."){
                "0.00"
            }else
                formattedAmount
        }catch (e:Exception){
            number
        }

    }


    fun formatDecimalNumber(number:Double, digitNumber:Int= 2):String{
        return ("%.${digitNumber}f".format(
            Locale.ENGLISH,
            number
        ))
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

    fun prettyCount(number: Long): String {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')

        val value = floor(log10(number.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                number / 10.0.pow((base * 3).toDouble())
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(number)
        }
    }

    fun containSpecialCharacter(value: String):Boolean{
        val special: Pattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
        return special.matcher(value).find()
    }
}