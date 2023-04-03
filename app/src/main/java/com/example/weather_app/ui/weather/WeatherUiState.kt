package com.example.weather_app.ui.weather

import com.example.weather_app.data.WeatherModel

data class WeatherUiState(
    val city: String = "",
    val timezone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val currentTime: String = "",
    val temperature: Double = 0.0,
    val weatherCode: Int = 0,
    val windSpeed: Double = 0.0,
    val windDirection: Double = 0.0,
    val hourlyTime: List<String> = emptyList(),
    val temperature_2m: List<Double> = emptyList()
)

fun WeatherUiState.toWeatherModel(): WeatherModel = WeatherModel(
    city = city,
    timezone = timezone,
    latitude = latitude,
    longitude = longitude,
    currentTime = currentTime,
    temperature = temperature,
    weatherCode = weatherCode,
    windSpeed = windSpeed,
    windDirection = windDirection,
    hourlyTime = hourlyTime,
    temperature_2m = temperature_2m
)

fun WeatherModel.toWeatherUiState(): WeatherUiState = WeatherUiState(
    city = city,
    timezone = timezone,
    latitude = latitude,
    longitude = longitude,
    currentTime = currentTime,
    temperature = temperature,
    weatherCode = weatherCode,
    windSpeed = windSpeed,
    windDirection = windDirection,
    hourlyTime = hourlyTime,
    temperature_2m = temperature_2m
)

fun WeatherUiState.isValid(): Boolean {
    return city.isNotEmpty() &&
            timezone.isNotEmpty() &&
            latitude != 0.0 &&
            longitude != 0.0 &&
            currentTime.isNotEmpty() &&
            temperature != 0.0 &&
            weatherCode != 0 &&
            windSpeed != 0.0 &&
            windDirection != 0.0 &&
            hourlyTime.isNotEmpty() &&
            temperature_2m.isNotEmpty()
}