package com.example.weather_app.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherModel(
    @Embedded val cityInfo: CityModel,
    val latitude: Double,
    val longitude: Double,
    @Embedded val currentWeather: CurrentWeatherModel,
    @Embedded val hourly: Hourly,
)

data class CityModel (
    @PrimaryKey val city: String,
    val timezone: String,
    val latitude: Double,
    val longitude: Double
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
    val temperature_2m: List<Double>
)
