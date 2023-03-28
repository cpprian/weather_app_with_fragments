package com.example.weather_app.model

import androidx.room.*
import com.example.weather_app.util.DoubleListConverter
import com.example.weather_app.util.StringListConverter

@Entity(tableName = "weather_table")
@TypeConverters(StringListConverter::class, DoubleListConverter::class)
data class WeatherModel(
    @PrimaryKey
    val city: String,
    val timezone: String,
    val latitude: Double,
    val longitude: Double,
    val currentTime: String,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val windDirection: Double,
    val hourlyTime: List<String>,
    val temperature_2m: List<Double>
)
