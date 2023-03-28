package com.example.weather_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherModel(
    val city: String,
    val timezone: String,
    val latitude: Double,
    val longitude: Double,
    val currentWeather: CurrentWeatherModel,
    val hourly: Hourly,
    val hourly_units: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class CurrentWeatherModel(
    val time: String,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
    val windDirection: Double
)

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
)
