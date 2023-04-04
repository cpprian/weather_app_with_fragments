package com.example.weather_app.data

import androidx.room.*

@Entity(tableName = "weather_table")
data class WeatherModel(
    @PrimaryKey
    val city: String,
    val timezone: String,
    val latitude: Double,
    val longitude: Double,
    val currentTime: String,
    val temperature: Double,
    val temperatureUnit: String,
    val weatherCode: Int,
    val windSpeed: Double,
    val windDirection: Double,
    val hourlyTime: String,
    val temperature_2m: String
)
