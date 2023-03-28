package com.example.weather_app.util

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromString(stringList: String?): List<String>? {
        return stringList?.split(",")?.map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>?): String? {
        return stringList?.joinToString(",")
    }
}

class DoubleListConverter {
    @TypeConverter
    fun fromString(stringList: String?): List<Double>? {
        return stringList?.split(",")?.map { it.toDouble() }
    }

    @TypeConverter
    fun toString(stringList: List<Double>?): String? {
        return stringList?.joinToString(",")
    }
}