package com.msharialsayari.musrofaty.layer_data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class Convertors {

    companion object {
        @JvmStatic
        @TypeConverter
        fun fromStringToStringList(value: String?): List<String> {
            value?.let {
                val listType: Type = object : TypeToken<List<String>>() {}.type
                return Gson().fromJson(it, listType)
            } ?: kotlin.run { return emptyList() }
        }

        @JvmStatic
        @TypeConverter
        fun fromStringListToString(list: List<String>?): String {
            val gson = Gson()
            list?.let {
                return gson.toJson(list)
            } ?: kotlin.run { return gson.toJson("") }
        }

        @JvmStatic
        @TypeConverter
        fun toLocalDateTime(value: Long): LocalDateTime? {
            return LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC)
        }

        @JvmStatic
        @TypeConverter
        fun fromLocalDateTime(ldt: LocalDateTime): Long {
            return ldt.toEpochSecond(ZoneOffset.UTC)
        }




    }
}