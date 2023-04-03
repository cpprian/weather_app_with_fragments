package com.example.weather_app.data

import kotlinx.coroutines.flow.Flow

class OfflineWeatherRepository(private val weatherDao: WeatherDao): WeatherRepository {
    override fun getAllWeather(): Flow<List<WeatherModel>> = weatherDao.getAllWeather()

    override fun getWeather(city: String): Flow<WeatherModel?> = weatherDao.getWeather(city)

    override suspend fun insertWeather(weather: WeatherModel) = weatherDao.insertWeather(weather)

    override suspend fun deleteWeather(weather: WeatherModel) = weatherDao.deleteWeather(weather.city)

    override suspend fun updateWeather(weather: WeatherModel) = weatherDao.updateWeather(
        weather.city,
        weather.currentTime,
        weather.temperature,
        weather.weatherCode,
        weather.windSpeed,
        weather.windDirection,
        weather.hourlyTime,
        weather.temperature_2m
    )
}