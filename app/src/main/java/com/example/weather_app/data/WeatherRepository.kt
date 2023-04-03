package com.example.weather_app.data

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getAllWeather(): Flow<List<WeatherModel>>

    fun getWeather(city: String): Flow<WeatherModel?>

    suspend fun insertWeather(weather: WeatherModel)

    suspend fun deleteWeather(weather: WeatherModel)

    suspend fun updateWeather(weather: WeatherModel)
}